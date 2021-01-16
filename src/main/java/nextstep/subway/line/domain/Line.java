package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void changeLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
            int newDistance = 0;
            //비즈니스 로직 처리
            //sections.add(section);
            // 잘못된 요청, 새로운 구간의 상행역 하행역이 기존 구간의 역에 포함되어 있는지 확인 등등
            //* 기존_구간_정보와_비교해_등록할_수_있는_구간인지_확인(새로운_구간)

            // 1번 케이스인 기존 구간 업데이트
            //*상행역_강남_구간 = 상행역으로_강남역을_가진_구간찾기(새로운_구간_상행역);
            Section sectionBetweenUpstation = findIfSameUpstation(section);
            // 2번 케이스인 기존 구간 업데이트
            //*하행역_선릉_구간 = 하행역으로_선릉역을_가진_구간찾기(새로운_구간_하행역);
            Section sectionBetweenDownstation = findIfSameDownstation(section);

            //*if (상행역_강남_구간_존재) {
            //*    상행역_강남_구간의_하행역 = 새로운_구간의_하행역
            //*    상행역_강남_구간의_거리 = 상행역_강남_구간의_거리 - 새로운_구간의_거리
            //*}
            if (sectionBetweenUpstation != null) {
                int count = sections.indexOf(sectionBetweenUpstation);

                newDistance = sectionBetweenUpstation.getDistance() - section.getDistance();
                //기존 구간 제거
                sections.remove(count);
                //새로 생길 구간 추가
                sections.add(count, new Section(sectionBetweenUpstation.getUpStation(), section.getDownStation(), section.getDistance()));
                //새로 등록 요청한 구간 추가
                sections.add(count+1, new Section(section.getDownStation(), sectionBetweenUpstation.getDownStation(), newDistance));
            }

            //*if (하행역_선릉_구간_존재) {
            //*    하행역_선릉_구간의_상행역 = 새로운_구간의_상행역
            //*    하행역_선릉_구간의_거리 = 하행역_선릉_구간의_거리 - 새로운_구간의_거리
            //*}
            if (sectionBetweenDownstation != null) {
                int count = sections.indexOf(sectionBetweenDownstation);

                newDistance = sectionBetweenDownstation.getDistance() - section.getDistance();
                //기존 구간 제거
                sections.remove(count);
                //새로 생길 구간 추가
                sections.add(count, new Section(sectionBetweenDownstation.getUpStation(), section.getUpStation(), newDistance));
                //새로 등록 요청한 구간 추가
                sections.add(count+1, new Section(section.getUpStation(), sectionBetweenDownstation.getDownStation(), section.getDistance()));

            }
            //*노선_구간_추가(새로운_구간_추가) // 새로운 구간 추가, 새로운 상행 종점역 추가, 새로운 하행 종점역 추가
            // 기존에 세션정보가 없을 경우
            addSectionRestCaes(section, sectionBetweenUpstation, sectionBetweenDownstation);
            section.setLine(this);
    }

    private void addSectionRestCaes(Section section, Section sectionBetweenUpstation, Section sectionBetweenDownstation) {
        if (sectionBetweenUpstation == null && sectionBetweenDownstation == null) {
            addNewSectionBased(section);
        }

        if (sections.size() == 0) {
            sections.add(section);
        }
    }

    private boolean addNewSectionBased(Section section) {
        for (Section sectionValue: sections) {
            if (sectionValue.getUpStation() == section.getUpStation() && sectionValue.getDownStation() == section.getDownStation()) {
                throw new IllegalArgumentException("새로운 구간은 기존 구간과 같을 수 없습니다!");
            }

            if (sectionValue.getUpStation() == section.getDownStation()) {
                sections.add(sections.indexOf(sectionValue), new Section(section.getUpStation(), section.getDownStation(), section.getDistance()));
                return true;
            }

            if (sectionValue.getDownStation() == section.getUpStation()) {
                sections.add(sections.indexOf(sectionValue)+1, new Section(section.getUpStation(), section.getDownStation(), section.getDistance()));
                return true;
            }
        }
        return false;
    }

    private Section findIfSameUpstation(Section section) {
        for (Section sectionUpstation: sections) {
            if (sectionUpstation.getUpStation() == section.getUpStation() && sectionUpstation.getDownStation() != section.getDownStation() && sectionUpstation.getDistance() > section.getDistance()) {
                return sectionUpstation;
            }
        }
        return null;
    }

    private Section findIfSameDownstation(Section section) {
        for (Section sectionDownstation: sections) {
            if (sectionDownstation.getDownStation() == section.getDownStation() && sectionDownstation.getUpStation() != section.getUpStation() && sectionDownstation.getDistance() > section.getDistance()) {
                return sectionDownstation;
            }
        }
        return null;
    }

    public void changeSection(List<Section> newSection) {
        sections = newSection;
    }

    public Long getId() { return id; }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }
}

