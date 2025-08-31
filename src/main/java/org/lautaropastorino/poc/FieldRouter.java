package org.lautaropastorino.poc;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.transforms.Transformation;
import org.apache.kafka.connect.transforms.util.SimpleConfig;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.kafka.connect.transforms.util.Requirements.requireMap;
import static org.apache.kafka.connect.transforms.util.Requirements.requireStruct;

public class FieldRouter<R extends ConnectRecord<R>> implements Transformation<R> {
    private static final String PURPOSE = "extracting record value field to modify destination topic";
    private static final Pattern TOPIC = Pattern.compile("${topic}", Pattern.LITERAL);
    private static final Pattern FIELD = Pattern.compile("${field}", Pattern.LITERAL);

    private String fieldName;
    private DateTimeFormatter sourceDateFormat;
    private DateTimeFormatter destDateFormat;
    private String destTopicFormat;

    @Override
    public void configure(Map<String, ?> props) {
        final SimpleConfig config = new SimpleConfig(FieldRouterConfig.CONFIG_DEF, props);
        this.fieldName = config.getString(FieldRouterConfig.FIELD_NAME);
        this.sourceDateFormat = DateTimeFormatter.ofPattern(config.getString(FieldRouterConfig.SOURCE_DATE_FORMAT));
        this.destDateFormat = DateTimeFormatter.ofPattern(config.getString(FieldRouterConfig.DEST_DATE_FORMAT));
        this.destTopicFormat = config.getString(FieldRouterConfig.DEST_TOPIC_FORMAT);
    }

    @Override
    public R apply(R record) {
        String fieldValue = getFieldValue(record.value());
        LocalDate parsedDate = LocalDate.parse(fieldValue, sourceDateFormat);

        return record.newRecord(
                buildTopicName(record.topic(), parsedDate.format(destDateFormat)),
                record.kafkaPartition(),
                record.keySchema(), record.key(),
                record.valueSchema(), record.value(),
                record.timestamp()
        );
    }

    private String getFieldValue(Object recordValue) {
        if (recordValue instanceof Struct) {
            Struct valueStruct = requireStruct(recordValue, PURPOSE);
            return valueStruct.getString(fieldName);
        }

        if (recordValue instanceof Map) {
            final Map<String, Object> value = requireMap(recordValue, PURPOSE);
            return value.get(fieldName).toString();
        }

        throw new DataException("Unsupported value type: " + recordValue.getClass().getName());
    }

    private String buildTopicName(String originTopic, String parsedDate) {
        final String topicReplace = TOPIC.matcher(destTopicFormat).replaceAll(Matcher.quoteReplacement(originTopic));
        return FIELD.matcher(topicReplace).replaceAll(Matcher.quoteReplacement(parsedDate));
    }

    @Override
    public ConfigDef config() {
        return FieldRouterConfig.CONFIG_DEF;
    }

    @Override
    public void close() {

    }
}
