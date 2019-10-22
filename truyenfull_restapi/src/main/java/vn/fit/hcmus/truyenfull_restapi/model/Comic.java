package vn.fit.hcmus.truyenfull_restapi.model;


import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "comic")
public class Comic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String urlname;

    private String author;

    private String source;

    private String status;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "comic_id")
    private List<Chapter> chapterList = new ArrayList<>();

//    Mapping voi Category
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name="comic_category",
    joinColumns = @JoinColumn(name = "comic_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categoryList;

//    Mapping voi Catalog = Danh sách trên truyenfull.net
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name="comic_catalog",
        joinColumns = @JoinColumn(name = "comic_id"),
        inverseJoinColumns = @JoinColumn(name = "catalog_id"))
    private List<Catalog> catalogList;
}
