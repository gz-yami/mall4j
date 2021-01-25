身为服务器的开发者，我们是无法相信用户输入的任何东西的。比如：金额不能从前端传过来，使用会失效的token等。当然，用户除了会传入一些假数据，也会传入一些假的脚本，比较出名的就是**xss攻击**

网上有很多说解决xss攻击的方法，有很多都是和前端有关，而实际上，在后台这最后一个防御当中，是最为重要的。

在mall4j这个项目里面，使用了一个过滤器 `XssFilter`

```
public class XssFilter implements Filter {
    Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        
        logger.info("uri:{}",req.getRequestURI());
        // xss 过滤
        chain.doFilter(new XssWrapper(req), resp);
    }
}

```

主要是通过 `new XssWrapper(req)` 这个对象进行一系列的过滤，而 `XssWrapper` 是通过`Jsoup`进行用户输入的一系列过滤。毕竟专业的事情要交给专业的人来搞定。就此，我们通过简单的设置就完成了对**xss攻击**的防御。

```java
public class XssWrapper extends HttpServletRequestWrapper {
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public XssWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 对数组参数进行特殊字符过滤
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    /**
     * 对参数中特殊字符进行过滤
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (StrUtil.isBlank(value)) {
            return value;
        }
        return cleanXSS(value);
    }

    /**
     * 获取attribute,特殊字符过滤
     */
    @Override
    public Object getAttribute(String name) {
        Object value = super.getAttribute(name);
        if (value instanceof String && StrUtil.isNotBlank((String) value)) {
            return cleanXSS((String) value);
        }
        return value;
    }

    /**
     * 对请求头部进行特殊字符过滤
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (StrUtil.isBlank(value)) {
            return value;
        }
        return cleanXSS(value);
    }

    private String cleanXSS(String value) {
        return XssUtil.clean(value);
    }
}

```

这里面最主要的方法就是`XssUtil.clean(value)` -> `Jsoup.clean(content, "", WHITE_LIST, OUTPUT_SETTINGS)`  这面最总要的是有个白名单列表 `WHITE_LIST` 来自，我们仔细观察白名单列表会发现这里面是部分携带html的部分标签进入，从而防止xss攻击

```java
new Whitelist().addTags(
                        "a", "b", "blockquote", "br", "caption", "cite", "code", "col",
                        "colgroup", "dd", "div", "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6",
                        "i", "img", "li", "ol", "p", "pre", "q", "small", "span", "strike", "strong",
                        "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u",
                        "ul")
    
                .addAttributes("a", "href", "title")
                .addAttributes("blockquote", "cite")
                .addAttributes("col", "span", "width")
                .addAttributes("colgroup", "span", "width")
                .addAttributes("img", "align", "alt", "height", "src", "title", "width")
                .addAttributes("ol", "start", "type")
                .addAttributes("q", "cite")
                .addAttributes("table", "summary", "width")
                .addAttributes("td", "abbr", "axis", "colspan", "rowspan", "width")
                .addAttributes(
                        "th", "abbr", "axis", "colspan", "rowspan", "scope",
                        "width")
                .addAttributes("ul", "type")

                .addProtocols("a", "href", "ftp", "http", "https", "mailto")
                .addProtocols("blockquote", "cite", "http", "https")
                .addProtocols("cite", "cite", "http", "https")
                .addProtocols("img", "src", "http", "https")
                .addProtocols("q", "cite", "http", "https")
```

