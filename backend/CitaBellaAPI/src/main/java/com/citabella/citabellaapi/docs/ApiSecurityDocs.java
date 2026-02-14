package com.citabella.citabellaapi.docs;

public class ApiSecurityDocs {
    public static final String ADMIN_EMPLOYEE =
            "Requires authentication. Allowed roles: ADMIN, EMPLOYEE.";
    public static final String ADMIN =
            "Requires authentication. Allowed roles: ADMIN.";
    public static final String ADMIN_EMPLOYEE_CLIENT =
            "Requires authentication. Allowed roles: ADMIN, EMPLOYEE, CLIENT..";
    public static final String ANYONE =
            "No authentication required.";
}
