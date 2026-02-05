package nomadrealms.event.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.HasInventory;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.DropItemEvent;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.item.Item;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.context.game.zone.DeckCollection;

public class SyncedEventDerializer {

    public static byte[] serialize(SyncedEvent event) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {

            if (event instanceof CardPlayedEvent) {
                dos.writeByte(1);
                serializeCardPlayedEvent((CardPlayedEvent) event, dos);
            } else if (event instanceof DropItemEvent) {
                dos.writeByte(2);
                serializeDropItemEvent((DropItemEvent) event, dos);
            } else {
                throw new IllegalArgumentException("Unknown event type: " + event.getClass());
            }

            dos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SyncedEvent deserialize(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             DataInputStream dis = new DataInputStream(bis)) {

            byte type = dis.readByte();
            if (type == 1) {
                return deserializeCardPlayedEvent(dis);
            } else if (type == 2) {
                return deserializeDropItemEvent(dis);
            } else {
                throw new IllegalArgumentException("Unknown event type byte: " + type);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void serializeCardPlayedEvent(CardPlayedEvent event, DataOutputStream dos) throws IOException {
        // Serialize Card: GameCard name, Deck Index, Card Index in Deck
        WorldCard card = event.card();
        dos.writeUTF(card.card().name());

        // Find deck index
        CardPlayer source = event.source();
        int deckIndex = getDeckIndex(source, (Deck) card.zone());
        dos.writeInt(deckIndex);

        // Find card index in deck
        int cardIndex = ((Deck) card.zone()).getCards().indexOf(card);
        dos.writeInt(cardIndex);

        // Serialize Source: Tile Coordinate
        writeTileCoordinate(source.tile().coord(), dos);

        // Serialize Target
        Target target = event.target();
        if (target instanceof Tile) {
            dos.writeByte(1); // Target is Tile
            writeTileCoordinate(((Tile) target).coord(), dos);
        } else if (target instanceof Actor) {
            dos.writeByte(2); // Target is Actor
            writeTileCoordinate(((Actor) target).tile().coord(), dos);
        } else {
            throw new IllegalArgumentException("Unknown target type: " + target.getClass());
        }
    }

    private static SyncedEvent deserializeCardPlayedEvent(DataInputStream dis) throws IOException {
        String cardName = dis.readUTF();
        int deckIndex = dis.readInt();
        int cardIndex = dis.readInt();
        TileCoordinate sourceCoord = readTileCoordinate(dis);
        byte targetType = dis.readByte();
        TileCoordinate targetCoord = readTileCoordinate(dis);

        return new NetworkedCardPlayedEvent(cardName, deckIndex, cardIndex, sourceCoord, targetType, targetCoord);
    }

    private static void serializeDropItemEvent(DropItemEvent event, DataOutputStream dos) throws IOException {
        // Serialize Source
        Nomad source = (Nomad) event.source();
        writeTileCoordinate(source.tile().coord(), dos);

        // Serialize Item Name
        dos.writeUTF(event.item().item().name());

        // Serialize Tile
        writeTileCoordinate(event.tile().coord(), dos);
    }

    private static SyncedEvent deserializeDropItemEvent(DataInputStream dis) throws IOException {
        TileCoordinate sourceCoord = readTileCoordinate(dis);
        String itemName = dis.readUTF();
        TileCoordinate tileCoord = readTileCoordinate(dis);
        return new NetworkedDropItemEvent(sourceCoord, itemName, tileCoord);
    }

    private static int getDeckIndex(CardPlayer player, Deck deck) {
        DeckCollection decks = player.deckCollection();
        if (decks.deck1() == deck) return 0;
        if (decks.deck2() == deck) return 1;
        if (decks.deck3() == deck) return 2;
        if (decks.deck4() == deck) return 3;
        throw new IllegalArgumentException("Card is not in any of the player's decks");
    }

    private static void writeTileCoordinate(TileCoordinate coord, DataOutputStream dos) throws IOException {
        dos.writeInt(coord.x());
        dos.writeInt(coord.y());
        dos.writeInt(coord.chunk().x());
        dos.writeInt(coord.chunk().y());
        dos.writeInt(coord.chunk().zone().x());
        dos.writeInt(coord.chunk().zone().y());
        dos.writeInt(coord.chunk().zone().region().x());
        dos.writeInt(coord.chunk().zone().region().y());
    }

    private static TileCoordinate readTileCoordinate(DataInputStream dis) throws IOException {
        int tx = dis.readInt();
        int ty = dis.readInt();
        int cx = dis.readInt();
        int cy = dis.readInt();
        int zx = dis.readInt();
        int zy = dis.readInt();
        int rx = dis.readInt();
        int ry = dis.readInt();
        return new TileCoordinate(
                new ChunkCoordinate(
                        new ZoneCoordinate(
                                new RegionCoordinate(rx, ry),
                                zx, zy),
                        cx, cy),
                tx, ty);
    }

    public static class NetworkedCardPlayedEvent extends CardPlayedEvent implements SyncedEvent {
        private final String cardName;
        private final int deckIndex;
        private final int cardIndex;
        private final TileCoordinate sourceCoord;
        private final byte targetType;
        private final TileCoordinate targetCoord;

        // Resolved fields
        private WorldCard resolvedCard;
        private CardPlayer resolvedSource;
        private Target resolvedTarget;

        public NetworkedCardPlayedEvent(String cardName, int deckIndex, int cardIndex, TileCoordinate sourceCoord, byte targetType, TileCoordinate targetCoord) {
            super(); // Initialize with defaults
            this.cardName = cardName;
            this.deckIndex = deckIndex;
            this.cardIndex = cardIndex;
            this.sourceCoord = sourceCoord;
            this.targetType = targetType;
            this.targetCoord = targetCoord;
        }

        @Override
        public void link(World world) {
            Tile sourceTile = world.getTile(sourceCoord);
            if (sourceTile == null) throw new RuntimeException("Source tile not found: " + sourceCoord);
            resolvedSource = (CardPlayer) sourceTile.actor();
            if (resolvedSource == null) throw new RuntimeException("Source actor not found at " + sourceCoord);

            Deck deck = getDeck(resolvedSource, deckIndex);
            List<WorldCard> cards = deck.getCards();
            if (cardIndex >= cards.size()) {
                 throw new RuntimeException("Card index out of bounds: " + cardIndex);
            }
            resolvedCard = cards.get(cardIndex);

            // Verify card name matches
            if (!resolvedCard.card().name().equals(cardName)) {
                System.err.println("Warning: Card name mismatch. Expected " + cardName + ", got " + resolvedCard.card().name());
            }

            Tile targetTile = world.getTile(targetCoord);
            if (targetType == 1) {
                resolvedTarget = targetTile;
            } else {
                resolvedTarget = targetTile.actor();
            }
        }

        private Deck getDeck(CardPlayer player, int index) {
            switch (index) {
                case 0: return player.deckCollection().deck1();
                case 1: return player.deckCollection().deck2();
                case 2: return player.deckCollection().deck3();
                case 3: return player.deckCollection().deck4();
                default: throw new IllegalArgumentException("Invalid deck index: " + index);
            }
        }

        @Override
        public WorldCard card() {
            return resolvedCard;
        }

        @Override
        public CardPlayer source() {
            return resolvedSource;
        }

        @Override
        public Target target() {
            return resolvedTarget;
        }

        @Override
        public String toString() {
            return "NetworkedCardPlayedEvent{" +
                    "card=" + (resolvedCard != null ? resolvedCard.card() : cardName) +
                    ", source=" + resolvedSource +
                    ", target=" + resolvedTarget +
                    '}';
        }
    }

    public static class NetworkedDropItemEvent extends DropItemEvent implements SyncedEvent {
        private final TileCoordinate sourceCoord;
        private final String itemName;
        private final TileCoordinate tileCoord;

        private WorldItem resolvedItem;
        private HasInventory resolvedSource;
        private Tile resolvedTile;

        public NetworkedDropItemEvent(TileCoordinate sourceCoord, String itemName, TileCoordinate tileCoord) {
            super();
            this.sourceCoord = sourceCoord;
            this.itemName = itemName;
            this.tileCoord = tileCoord;
        }

        @Override
        public void link(World world) {
            Tile sourceTile = world.getTile(sourceCoord);
            resolvedSource = (HasInventory) sourceTile.actor();

            // Find item by name
            for (WorldItem item : resolvedSource.inventory().items()) {
                if (item.item().name().equals(itemName)) {
                    resolvedItem = item;
                    break;
                }
            }
            if (resolvedItem == null) {
                // throw new RuntimeException("Item not found: " + itemName);
                // Be resilient?
            }

            resolvedTile = world.getTile(tileCoord);
        }

        @Override
        public WorldItem item() {
            return resolvedItem;
        }

        @Override
        public HasInventory source() {
            return resolvedSource;
        }

        @Override
        public Tile tile() {
            return resolvedTile;
        }

        @Override
        public String toString() {
            return "NetworkedDropItemEvent{" +
                    "item=" + (resolvedItem != null ? resolvedItem.item() : itemName) +
                    ", source=" + resolvedSource +
                    '}';
        }
    }
}
