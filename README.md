# Rosemary, a collection of Thymeleaf extensions

## Versioned Resources
This dialect offers an attribute to handle versioning of HTML resource (js, css) for cache busting purposes.

### Configuration
Just add this dialect to the Thymelaf engine, passing the desired version as a constructor parameter.

```java
final TemplateEngine engine = new TemplateEngine();
engine.setDialects(Arrays.asList(
    ...
    new VersionedResourceDialect(version),
    ...
));

```

### Usage
Add a `version:append` attribute to any tag with a `src`/`href` attribute to append a version query parameter:

```html
<script type="text/javascript" src="/path/to/resource.js" version:append></script>
```

will be rendered as

```html
<script type="text/javascript" src="/path/to/resource.js?version=xxxx"></script>
```

Notes:
* if a query string is already present the version parameter will be concatenated using &amp;
* an already present `version` parameter will not be overridden
* empty `src`/`href` attributes will not be updated



