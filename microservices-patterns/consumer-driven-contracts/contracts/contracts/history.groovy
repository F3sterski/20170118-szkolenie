package contracts

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        urlPath('/history') {
            queryParameters {
                parameter 'limit': 5
            }
        }
    }
    response {
        status 200
        body("""[{
                    "input": "This is fine",
                    "time": "${regex('[0-9]{10}')}"
                },
                {
                    "input": "This is shit",
                    "time": "${regex('[0-9]{10}')}"
                },
                {
                    "input": "wtf",
                    "time": "${regex('[0-9]{10}')}"
                },
                {
                    "input": "what the fuck",
                    "time": "${regex('[0-9]{10}')}"
                },
                {
                    "input": "what thefuck",
                    "time": "${regex('[0-9]{10}')}"
                }

        ]""")
        headers {
            header('Content-Type': value(regex('application/json.*')))
        }
    }
}
