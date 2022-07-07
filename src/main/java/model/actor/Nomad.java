package model.actor;

import static model.actor.CardPlayerSerializationFormats.NOMAD;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import derealizer.format.Serializable;
import graphics.displayer.NomadDisplayer;
import model.id.NomadId;

public class Nomad extends CardPlayer implements Serializable {

	private transient NomadDisplayer displayer;

	public Nomad() {
		super(20);
		displayer = new NomadDisplayer(id);
	}

	public Nomad(long id, NomadDisplayer displayer) {
		super(20, id);
		this.displayer = displayer;
	}

	public Nomad(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public Nomad copy() {
		Nomad copy = new Nomad(id, displayer);
		return super.copyTo(copy);
	}

	@Override
	public NomadId id() {
		return new NomadId(id);
	}

	@Override
	public NomadDisplayer displayer() {
		return displayer;
	}

	@Override
	public String description() {
		return "A nomad with " + health + "/" + maxHealth + " health";
	}

	@Override
	public CardPlayerSerializationFormats formatEnum() {
		return NOMAD;
	}

	@Override
	public void read(SerializationReader reader) {
		super.read(reader);
	}

	@Override
	public void write(SerializationWriter writer) {
		super.write(writer);
	}

}
