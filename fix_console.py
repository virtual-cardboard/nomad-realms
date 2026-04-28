import re

with open('./nomad-realms/src/main/java/nomadrealms/render/ui/custom/console/Console.java', 'r') as f:
    content = f.read()

search_str = """		} else if (cmd.equalsIgnoreCase("SAY")) {
			if (parts.length < 2) {
				return "Usage: SAY <text>";
			}
			Nomad nomad = null;
			for (Actor actor : gameState.world.lookup().all()) {
				if (actor instanceof Nomad) {
					nomad = (Nomad) actor;
					break;
				}
			}
			if (nomad != null) {
				StringBuilder text = new StringBuilder();
				for (int i = 1; i < parts.length; i++) {
					text.append(parts[i]).append(" ");
				}
				nomad.say(text.toString().trim(), 3000);
				return null;
			}
			return "Nomad not found in world.";
		}"""

replace_str = """		} else if (cmd.equalsIgnoreCase("SAY")) {
			if (parts.length < 2) {
				return "Usage: SAY <text>";
			}
			if (re.localPlayer != null && re.localPlayer.cardPlayer(gameState.world) != null) {
				StringBuilder text = new StringBuilder();
				for (int i = 1; i < parts.length; i++) {
					text.append(parts[i]).append(" ");
				}
				re.localPlayer.cardPlayer(gameState.world).say(text.toString().trim(), 3000);
				return null;
			}
			return "Player not found.";
		}"""

content = content.replace(search_str, replace_str)

with open('./nomad-realms/src/main/java/nomadrealms/render/ui/custom/console/Console.java', 'w') as f:
    f.write(content)
