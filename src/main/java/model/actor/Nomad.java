package model.actor;

import static model.actor.CardPlayerSerializationFormats.NOMAD;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import derealizer.format.Serializable;
import graphics.displayer.NomadDisplayer;
import model.id.NomadId;

public class Nomad extends CardPlayer implements Serializable {

	public Nomad() {
		super(20);
		setDisplayer(new NomadDisplayer());
	}

	private Nomad(long id, NomadDisplayer displayer) {
		super(20, id);
		setDisplayer(displayer);
	}

	public Nomad(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public Nomad copy() {
		return super.copyTo(new Nomad(longID(), (NomadDisplayer) displayer()));
	}

	@Override
	public NomadId id() {
		return new NomadId(id);
	}

	@Override
	public String description() {
		return "A nomad with " + health + "/" + maxHealth + " health";
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
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
