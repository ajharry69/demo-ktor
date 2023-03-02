<#import "../_layout.ftl" as layout />

<@layout.header>
    <form action="/articles" method="post">
        <p>
            <label for="title">Title</label>
            <input name="title" id="title" required>
        </p>
        <p>
            <label for="body">Body</label>
            <textarea name="body" id="body" required></textarea>
        </p>
        <p>
            <input type="submit" name="submit" value="Submit">
        </p>
    </form>
    <hr>
</@layout.header>