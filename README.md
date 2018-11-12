# Scramblies challenge

### Task 1
Complete the function (scramble str1 str2) that returns true if a portion of str1 characters can be rearranged to match str2, otherwise returns false

**Notes:**  

Only lower case letters will be used (a-z). No punctuation or digits will be included.
Performance needs to be considered

**Examples:**  

``` clojure
(scramble? “rekqodlw” ”world”) ;==> true  
(scramble? “cedewaraaossoqqyt” ”codewars”) ;==> true  
(scramble? “katas”  “steak”) ;==> false  
```
### Task 2
Create a web service that accepts two strings in a request and applies function scramble? from previous task to them.

### Task 3
Create a UI in ClojureScript with two inputs for strings and a scramble button. When the button is fired it should call the API from previous task and display a result.  

**Notes:**  

Please pay attention to tests, code readability and error cases.

## Usage

**all in one**  

```./build-and-run``` then visit ```http://localhost:3000```

**testing**  

```lein test```

**compiling js and css**  

```lein build``` 

**compiling js, css and jar**  

```lein build-all``` 

**launching dev server**

```lein launch```

**using jar**  

```java -jar target/challenge-0.1.0-standalone.jar -p 3000```





