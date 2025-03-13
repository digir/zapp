# GET endpoints

GET `/api/scaff/0000000000000000000000000000000000000000/options`



## Get next options
`/api/scaff/<scaff id>/options`
```json
{
    // Each scaff is a 'specialization' of an earlier one, implementing more and more specific details with each iteration

    // Each key is the scaff id which we may traverse to
    "abc": {
        "name": "My cool React scaff",
        "desc": "Scaff for a basic React template printing hello world to the console",
        "author": "conquer"
    },
    "123": {
        "name": "Folio",
        "desc": "A snazzy starter portfolio site written in Hugo",
        "author": "GreatLove"
    },
    // ...
}
```


## Get final generated templated file system at node
`/api/scaff/<scaff id>/render`

GET `/api/scaff/ahsfeijwenicon13x/render`:
```json
{
    "vars": {
        "message_var": {
            "desc": "",
            "type": "string",
        },
        "background_color": {
            "type": "option",
            "options": [ "red", "green", "blue" ]
        }
    },
    "files": {
        // object entries correspond to folders
        "src": {
            "index.html": """
                <body style='background-color: {{background_color}}'>
                    <h1> Hello world </h1>
                </body>
            """,
            "script.js": """
                console.log({{message_var}})
            """
        },

        // string entries correspond to files
        ".gitignore": """
            node_modules/
        """
    }
}
```





Empty
```json
{ "files": {} }
```

Effectively a massive recursive descent parser
"Additive version control"

node.js scaffold
```json
{
    "id": "afecuy3lew",
    "files": {
        ".gitignore": """
            <<template_variable1|Description>>
            some
            random/values
            <<template_variable2>>
            to/.gitignore
            <<template_variable3>>
        """,

        "package.json": [
            { "entity": "var", "name": "template_variable1" },
            { "entity": "txt", "body": "some\nrandom/values" },
            { "entity": "var", "name": "template_variable2" },
            { "entity": "txt", "body": "to/.gitignore" },
            { "entity": "var", "name": "template_variable3" },
        ],

        "src/index.html": {
            "$template_variable1": {},
            ""
        }
    },
}
```






```json
// scaff: "000"
// This is the empty 'starter' scaff
{
    "id": "000",
    "parent": "000", // The empty scaff is it's own parent

    "substitutions": {}
}

// scaff: abc
{
    "id": "abc",
    "parent": "000",

    "name": "python project template",
    "desc": "blah blah",

    "substitutions": {
        "A.py": {
            // "_" used as a placeholder key for when the file does not exist yet
            "_": """
                <<header_var>>
                // Print message twice
                print(<<message_var>>)
                print(<<message_var>>)
                <<body_var>>
            """
        }
    }
}


// scaff: 123
{
    "id": "123",
    "parent": "abc",

    "subtstitutions": {
        "A.py": {
            "header_var": """
                import numpy
                import matplotlib
            """,
            "body_var": "if (True) print('Hello world')",
            "message_var": "Hello, <<name>>"
            // Note that "body_var" is not populated, so instead it is left as-is, and may be populated further down-the-line
        },
        "B.py": { "_": "" } // B.py will get created as empty file. Since no template vars are defined, it can never be populated
    }
}

// scaff: ca7
{
    "id": "ca7",
    "parent": "abc",

    "subtstitutions": {
        "A.py": {
            "header_var": "from math import log2",
            "body_var": "x = log2(64)",
            "message_var": "Hello, <<name>>"
        }
    }
}
```