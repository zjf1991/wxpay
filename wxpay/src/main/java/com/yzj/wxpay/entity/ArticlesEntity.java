package com.yzj.wxpay.entity;

import java.util.List;

public class ArticlesEntity extends BaseMessageEntity {

    private Integer ArticleCount;
    private List Articles;

    public Integer getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(Integer articleCount) {
        ArticleCount = articleCount;
    }

    public List getArticles() {
        return Articles;
    }

    public void setArticles(List articles) {
        Articles = articles;
    }

}
