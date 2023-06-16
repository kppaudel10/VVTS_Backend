package global;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvts.dto.PredictionDto;

import java.io.IOException;

/**
 * @auther kul.paudel
 * @created at 2023-06-15
 */
public class PredictionDtoDeserializer extends JsonDeserializer<PredictionDto> {

    @Override
    public PredictionDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode node = mapper.readTree(jsonParser);

        // Extract values from the JSON node and create a PredictionDto object
        String message = node.get("message").asText();
        // Extract other fields and adjust the code accordingly

//        return new PredictionDto(message);
        // Return the PredictionDto object with the extracted values
        return null;
    }

}
