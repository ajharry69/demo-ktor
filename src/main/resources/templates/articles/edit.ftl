<#-- @ftlvariable name="article" type="ke.co.xently.models.Article" -->

<#import "../_layout.ftl" as layout >

<@layout.header>
    <div>
        <form action="/articles/${article.id}" method="post">
            <p>
                <label for="title">Title</label>
                <input name="title" id="title" value="${article.title}" required>
            </p>
            <p>
                <label for="body">Body</label>
                <textarea name="body" id="body" required>${article.body}</textarea>
            </p>
            <p>
                <input type="submit" name="_action" value="update">
            </p>
        </form>
    </div>
    <div>
        <form action="/articles/${article.id}" method="post">
            <p>
                <input type="submit" name="_action" value="delete">
            </p>
        </form>
    </div>
    <hr>
</@layout.header>