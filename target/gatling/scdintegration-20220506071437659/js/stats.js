var stats = {
    type: "GROUP",
name: "Global Information",
path: "",
pathFormatted: "group_missing-name-b06d1",
stats: {
    "name": "Global Information",
    "numberOfRequests": {
        "total": "2",
        "ok": "2",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "56",
        "ok": "56",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "196",
        "ok": "196",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "126",
        "ok": "126",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "70",
        "ok": "70",
        "ko": "-"
    },
    "percentiles1": {
        "total": "126",
        "ok": "126",
        "ko": "-"
    },
    "percentiles2": {
        "total": "161",
        "ok": "161",
        "ko": "-"
    },
    "percentiles3": {
        "total": "189",
        "ok": "189",
        "ko": "-"
    },
    "percentiles4": {
        "total": "195",
        "ok": "195",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "count": 2,
    "percentage": 100
},
    "group2": {
    "name": "800 ms < t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t > 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "count": 0,
    "percentage": 0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "2",
        "ok": "2",
        "ko": "-"
    }
},
contents: {
"req_email-api-2ac62": {
        type: "REQUEST",
        name: "Email API",
path: "Email API",
pathFormatted: "req_email-api-2ac62",
stats: {
    "name": "Email API",
    "numberOfRequests": {
        "total": "1",
        "ok": "1",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "196",
        "ok": "196",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "196",
        "ok": "196",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "196",
        "ok": "196",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "0",
        "ok": "0",
        "ko": "-"
    },
    "percentiles1": {
        "total": "196",
        "ok": "196",
        "ko": "-"
    },
    "percentiles2": {
        "total": "196",
        "ok": "196",
        "ko": "-"
    },
    "percentiles3": {
        "total": "196",
        "ok": "196",
        "ko": "-"
    },
    "percentiles4": {
        "total": "196",
        "ok": "196",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "count": 1,
    "percentage": 100
},
    "group2": {
    "name": "800 ms < t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t > 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "count": 0,
    "percentage": 0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "1",
        "ok": "1",
        "ko": "-"
    }
}
    },"req_gid-api-ba8e8": {
        type: "REQUEST",
        name: "GID API",
path: "GID API",
pathFormatted: "req_gid-api-ba8e8",
stats: {
    "name": "GID API",
    "numberOfRequests": {
        "total": "1",
        "ok": "1",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "56",
        "ok": "56",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "56",
        "ok": "56",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "56",
        "ok": "56",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "0",
        "ok": "0",
        "ko": "-"
    },
    "percentiles1": {
        "total": "56",
        "ok": "56",
        "ko": "-"
    },
    "percentiles2": {
        "total": "56",
        "ok": "56",
        "ko": "-"
    },
    "percentiles3": {
        "total": "56",
        "ok": "56",
        "ko": "-"
    },
    "percentiles4": {
        "total": "56",
        "ok": "56",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "count": 1,
    "percentage": 100
},
    "group2": {
    "name": "800 ms < t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t > 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "count": 0,
    "percentage": 0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "1",
        "ok": "1",
        "ko": "-"
    }
}
    }
}

}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#percentiles3").append(stat.percentiles3.total);
    $("#percentiles3OK").append(stat.percentiles3.ok);
    $("#percentiles3KO").append(stat.percentiles3.ko);

    $("#percentiles4").append(stat.percentiles4.total);
    $("#percentiles4OK").append(stat.percentiles4.ok);
    $("#percentiles4KO").append(stat.percentiles4.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
