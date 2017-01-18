package contracts

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        urlPath('/checkprofanity') {
            queryParameters {
                parameter 'text': 'This is shit'
            }
        }
    }
    response {
        status 200
        body([
                containsProfanity: true,
                input: 'This is shit',
                output: 'This is ****'
        ])
        headers {
            header('Content-Type': value(regex('application/json.*')))
        }
    }
}
