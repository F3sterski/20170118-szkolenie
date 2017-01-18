package contracts

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        urlPath('/checkprofanity') {
            queryParameters {
                parameter 'text': 'This sentence is ok'
            }
        }
    }
    response {
        status 200
        body([
                containsProfanity: false,
                input: 'This sentence is ok',
                output: value(stub(""), test(optional(execute('isEmpty($it)'))))
        ])
        headers {
            header('Content-Type': value(regex('application/json.*')))
        }
    }
}
