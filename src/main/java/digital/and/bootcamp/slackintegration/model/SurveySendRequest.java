package digital.and.bootcamp.slackintegration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class SurveySendRequest {
    private String surveyId;
    private List<String> questions;
}
