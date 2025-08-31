package org.lautaropastorino.poc;

import org.apache.kafka.common.config.ConfigDef;

import java.time.format.DateTimeFormatter;

public class FieldRouterConfig {
    public static final String FIELD_NAME = "field.name";
    public static final String SOURCE_DATE_FORMAT = "source.date.format";
    public static final String DEST_DATE_FORMAT = "dest.date.format";
    public static final String DEST_TOPIC_FORMAT = "dest.topic.format";

    public static final ConfigDef CONFIG_DEF = new ConfigDef()
                    .define(
                        FIELD_NAME,
                        ConfigDef.Type.STRING,
                        ConfigDef.NO_DEFAULT_VALUE,
                        new ConfigDef.NonEmptyString(),
                        ConfigDef.Importance.HIGH,
                        "Record field name used to build the topic name")
                    .define(
                        SOURCE_DATE_FORMAT,
                        ConfigDef.Type.STRING,
                        "yyyy-MM-dd'T'HH:mm:ss.S",
                        new ConfigDef.NonEmptyString(),
                        ConfigDef.Importance.HIGH,
                        "Source date field format")
                    .define(
                        DEST_DATE_FORMAT,
                        ConfigDef.Type.STRING,
                        "yyyy-MM",
                        new ConfigDef.NonEmptyString(),
                        ConfigDef.Importance.HIGH,
                        "Destination date field format")
                    .define(
                            DEST_TOPIC_FORMAT,
                        ConfigDef.Type.STRING,
                        "${topic}-${field}",
                        new ConfigDef.NonEmptyString(),
                        ConfigDef.Importance.HIGH,
                        "Destination topic format");
}
