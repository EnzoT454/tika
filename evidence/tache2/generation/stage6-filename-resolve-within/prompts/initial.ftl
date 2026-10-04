Generate tests for ${class_name}.${method_sig}.

The focal class, constructor signatures and method source are:
```java
${full_fm}
```

<#if other_method_sigs?has_content>
Other available signatures (bodies omitted, not empty implementations):
${other_method_sigs}
</#if>

Use the public constructor and public methods. Keep the tests short and focused
on the focal method; use standard-library fixtures rather than reflection.
