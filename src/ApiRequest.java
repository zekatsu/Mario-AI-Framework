import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class ApiData {
    public String model = "text-davinci-002";
    public String prompt;
    public double temperature = 0.3;
    public int max_tokens = 256;
    public double top_p = 1.0;
    public double frequency_penalty = 0.0;
    public double presence_penalty = 0.0;
    public String stop = "###";
    private String prePrompt = """
###
This creates a list of events that is likely to happen in Super Mario Bros. in chronological order, based on a description.
Game events consists of EventType and Object
EventType is one of BUMP, STOMP_KILL, FIRE_KILL, SHELL_KILL, FALL_KILL, JUMP, LAND, COLLECT, HURT, KICK, LOSE, WIN
Object is one of NONE, GOOMBA, RED_KOOPA, RED_KOOPA_WINGED, GREEN_KOOPA, GREEN_KOOPA_WINGED, BULLET_BILL, ENEMY_FLOWER, MUSHROOM, FIRE_FLOWER, SHELL, LIFE_MUSHROOM, BLOCK, QUESTION_BLOCK, COIN
description: Mario gets mushroom and jumps.
1. ITEM_GET, MUSHROOM
2. JUMP, NONE
###
description: Mario kills two goombas in a row, but hurt by third goomba.
1. STOMP_KILL, GOOMBA
2. STOMP_KILL, GOOMBA
3. HURT, GOOMBA
###
description:""";

    ApiData (String input) {
        this.prompt = String.format("%s %s\n", prePrompt, input);
    }
}

class Choices {
    public String text;
    public int index;
    public int logprobs;
    public String finish_reason;
}

class ResponseData {
    public String id;
    public String object;
    public int created;
    public String model;
    public Choices[] choices;
    public Usage usage;
}

class Usage {
    public int prompt_tokens;
    public int completion_tokens;
    public int total_tokens;
}

public class ApiRequest {
    public String result;

    private static String parse (String raw) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ResponseData responseData = mapper.readValue(raw, ResponseData.class);
        return responseData.choices[0].text;
    }

    ApiRequest (String input) throws IOException, InterruptedException {
        ApiData data = new ApiData(input);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(data);
        System.out.println(json);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/completions"))
                .header("Content-Type", "Application/json")
                .header("Authorization", Dotenv.load().get("OPENAI_API_KEY"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        result = parse(response.body());
    }
}