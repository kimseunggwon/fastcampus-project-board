package com.fastcampus.projectboard.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Table;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;



@Getter
@ToString(callSuper = true) // AuditingFields 까지 String
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})
@Entity
public class Article extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 key값 자동 증가
    private Long id;

    @Setter @ManyToOne(optional = false)
    private UserAccount userAccount; // 유저 정보 (ID)

    //@Setter 따로 설정한 이유는 이 컬럼만 set 해서 수정을 해주기 위해서
    @Setter
    @Column(nullable = false)
    private String title; // 제목 , not null
    @Setter
    @Column(nullable = false,length = 10000)
    private String content; // 본문 , not null
    @Setter
    private String hashtag; // 해시태그

    @ToString.Exclude   // ToString 제외
    @OrderBy("createdAt DESC") //오름차순
    @OneToMany(mappedBy = "article",cascade = CascadeType.ALL)  // 일대다 [1:N] 관계
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    // 모든 JPA Entiy hibernate 구현체를 사용으로 기본 생성자를 가지고 있어야 한다
    protected Article() {
    }

    private Article(UserAccount userAccount, String title, String content, String hashtag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(UserAccount userAccount,String title,String content,String hashtag) {
        return new Article(userAccount,title,content,hashtag);
    }

    // id 만 equals adn HashCode 사용 ( 두 객체를 비교하기 위해 )
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}








