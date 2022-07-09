package model.task;

import static derealizer.SerializationClassGenerator.generate;

import derealizer.format.Serializable;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;

public enum TaskSerializationFormats implements SerializationFormatEnum {

//	@FieldNames({ "task", "taskID", "taskType", "taskData" })
//	MOVE_TASK(types(INT, LONG, INT, INT), null),
	;

	private final SerializationFormat format;
	private final Class<? extends Serializable> pojoClass;

	private TaskSerializationFormats(SerializationFormat format, Class<? extends Serializable> pojoClass) {
		this.format = format;
		this.pojoClass = pojoClass;
	}

	@Override
	public SerializationFormat format() {
		return format;
	}

	@Override
	public Class<? extends Serializable> pojoClass() {
		return pojoClass;
	}

	public static void main(String[] args) {
		generate(TaskSerializationFormats.class, Task.class);
	}

}
