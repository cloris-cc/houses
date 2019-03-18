package org.cloris.houses.common.model;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Jackson Fang
 * Date:   2018/11/9
 * Time:   11:03
 */
@Data
public class House {
    private Long id;
    private String name;
    private Integer type;
    private Integer price;
    private String images;
    private Integer area;
    private Integer beds;
    private Integer baths;
    private Double rating;
    private String remarks;
    private String properties;
    private String floorPlan;
    private String tags;
    private Date createTime;
    private Integer cityId;
    private Integer communityId;
    private String address;
    private Integer state;
    /**
     * additional properties
     */
    private String firstImg;
    private List<String> imageList = new ArrayList<>(); // all the link of images.
    private List<String> floorPlanList = new ArrayList<>(); // all the link of floorPlan images.
    private List<MultipartFile> houseFiles;
    private List<MultipartFile> floorPlanFiles;
    private Long userId;
    private boolean bookmarked;
    private List<Long> ids;
    private String sort = "time_desc";

    public void setImages(String images) {
        this.images = images;
        if(!Strings.isNullOrEmpty(images)) {
            List<String> list = Splitter.on(",").splitToList(images);
            if(list.size() > 0) {
                this.firstImg = list.get(0);
                this.imageList = list;
            }
        }
    }
}
