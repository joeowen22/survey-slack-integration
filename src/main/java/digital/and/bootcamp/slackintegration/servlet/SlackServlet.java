package digital.and.bootcamp.slackintegration.servlet;

import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackAppServlet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import java.util.regex.Pattern;

@Slf4j
@WebServlet("/slack/events")
public class SlackServlet extends SlackAppServlet {
    public SlackServlet(App app) {
        super(app);

        app.blockAction(Pattern.compile("^submittedAnswer.+$"), (req, ctx) -> {
            /**
             * Items we need to send result to results service
             * - Survey Id
             * - Person Id
             * - Question
             * - Answer
             */
            String personId = req.getPayload().getUser().getId();
            // "You're having a great day!|SURVEY_ID"
            String[] idSplit = req.getPayload().getActions().get(0).getValue().split("\\|");
            String question = idSplit[0];
            String surveyId = idSplit[1];
            String answer = req.getPayload().getActions().get(0).getText().getText();

            log.info("Received: {}, {}, {}, {}", surveyId, personId, question, answer);

            return ctx.ack();
        });
    }
}