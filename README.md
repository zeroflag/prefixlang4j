# prefixlang4j

## Examples

```clojure
(or
    (and
        (member 'admin')
        (member 'datalake'))
    (or
        (username 'bob') 
        (username 'sam')
        (username 'tom')))
```
