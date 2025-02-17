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
This is game event generator for "Super Mario Bros."
This creates a list of events that is likely to happen in chronological order, based on given description.
Game events consists of Type and Parameter.
Type is one of [BUMP, STOMP_KILL, FIRE_KILL, SHELL_KILL, FALL_KILL, JUMP, LAND, COLLECT, HURT, KICK, LOSE, WIN]
Parameter is one of [NONE, GOOMBA, RED_KOOPA, RED_KOOPA_WINGED, GREEN_KOOPA, GREEN_KOOPA_WINGED, SPIKY, BULLET_BILL, ENEMY_FLOWER, MUSHROOM, FIRE_FLOWER, SHELL, COIN, BRICK, QUESTION_BLOCK]
###
description: Mario jumps and get two coins.
1. JUMP, NONE
2. COLLECT, COIN
3. COLLECT, COIN
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