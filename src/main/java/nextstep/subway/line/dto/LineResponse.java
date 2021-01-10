package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.dto.StationResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<StationResponse> stationResponses = new ArrayList<>();

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, LocalDateTime createdDate, LocalDateTime modifiedDate, List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.stationResponses = stationResponses;
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getCreatedDate(), line.getModifiedDate(), line.getStationResponses());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public List<StationResponse> getStationsResponses() {
        return stationResponses;
    }
}
