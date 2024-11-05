package com.tveu.jcode.code_service.api;

public class Path {

    public static final String VERSION = "/v1";
    public static final String ROOT = VERSION + "/code";
    public static final String ID = "{id}";


    public static final String SUBMISSION = ROOT + "/submission";

    public static final String SUBMISSION_GET = SUBMISSION + "/" + ID;
    public static final String SUBMISSION_POST = SUBMISSION;
    public static final String SUBMISSION_PATCH = SUBMISSION;

    public static final String PROBLEM = ROOT + "/problem";
    public static final String PROBLEM_GET = PROBLEM + "/" + ID;
    public static final String PROBLEM_PUT = PROBLEM + "/" + ID;
    public static final String PROBLEM_POST = PROBLEM;
    public static final String PROBLEM_DELETE = PROBLEM + "/" + ID;

    public static final String PROBLEM_SUBMISSION = ROOT + "/problem-submission";
    public static final String PROBLEM_SUBMISSION_POST = PROBLEM_SUBMISSION;

    public static final String TEST_CASE = ROOT + "/case";
    public static final String TEST_CASE_GET = TEST_CASE;
    public static final String TEST_CASE_POST = TEST_CASE;
    public static final String TEST_CASE_PUT = TEST_CASE + "/" + ID;
    public static final String TEST_CASE_DELETE = TEST_CASE + "/" + ID;
}
