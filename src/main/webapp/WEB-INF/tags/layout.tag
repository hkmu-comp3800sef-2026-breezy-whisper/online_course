<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>

<%-- Override JSTL locale with Spring's resolved locale (set by WebMvcConfig interceptor) --%>
<fmt:setLocale value="${requestScope.springLocale}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Flowbite CDN -->
    <link href="https://cdn.flowbite.com/flowbite.min.css" rel="stylesheet">
    <script src="https://unpkg.com/flowbite@latest/dist/flowbite.js"></script>

    <title><fmt:message key="${title}" /></title>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">

    <!-- Dynamic Navbar using Spring Security Taglib -->
    <nav class="bg-white shadow-md">
        <div class="max-w-7xl mx-auto px-4 py-3">
            <div class="flex justify-between items-center">
                <div class="flex space-x-6">
                    <a href="/" class="text-gray-700 hover:text-blue-600 font-medium">
                        <fmt:message key="nav.home" />
                    </a>

                    <sec:authorize access="isAnonymous()">
                        <a href="/login" class="text-gray-700 hover:text-blue-600 font-medium">
                            <fmt:message key="nav.login" />
                        </a>
                        <a href="/register" class="text-gray-700 hover:text-blue-600 font-medium">
                            <fmt:message key="nav.register" />
                        </a>
                    </sec:authorize>

                    <sec:authorize access="isAuthenticated()">
                        <a href="/lecture/list" class="text-gray-700 hover:text-blue-600 font-medium">
                            <fmt:message key="nav.lectures" />
                        </a>
                        <a href="/poll/list" class="text-gray-700 hover:text-blue-600 font-medium">
                            <fmt:message key="nav.polls" />
                        </a>
                        <a href="/user/profile" class="text-gray-700 hover:text-blue-600 font-medium">
                            <fmt:message key="nav.profile" />
                        </a>
                        <a href="/user/comments" class="text-gray-700 hover:text-blue-600 font-medium">
                            <fmt:message key="nav.myComments" />
                        </a>

                        <sec:authorize access="hasRole('TEACHER')">
                            <a href="/admin/users" class="text-gray-700 hover:text-blue-600 font-medium">
                                <fmt:message key="nav.userManagement" />
                            </a>
                        </sec:authorize>
                    </sec:authorize>
                </div>

                <div class="flex items-center space-x-4">
                    <sec:authorize access="isAuthenticated()">
                        <span class="text-gray-600 text-sm">
                            <fmt:message key="nav.loggedInAs" />: <sec:authentication property="name" />
                        </span>
                        <form action="/logout" method="post" class="inline">
                            <input type="hidden" name="_csrf" value="${_csrf.token}" />
                            <button type="submit" class="text-gray-700 hover:text-red-600 font-medium">
                                <fmt:message key="nav.logout" />
                            </button>
                        </form>
                    </sec:authorize>
                </div>
            </div>
        </div>
    </nav>

    <!-- Language Switcher -->
    <div class="bg-gray-100 px-4 py-2 text-right">
        <a href="?lang=en" class="text-blue-600 hover:underline mx-2">English</a>
        <span class="text-gray-400">|</span>
        <a href="?lang=zh" class="text-blue-600 hover:underline mx-2">中文</a>
    </div>

    <!-- Page Content -->
    <main class="flex-grow max-w-7xl w-full mx-auto px-4 py-8">
        <jsp:doBody />
    </main>

    <!-- TypeScript compiled JavaScript -->
    <script src="<c:url value='/js/index.js'/>"></script>

    <!-- Footer -->
    <footer class="bg-gray-800 text-white text-center py-4 mt-auto">
        <p>&copy; 2026 Online Course Website. All rights reserved.</p>
    </footer>

</body>
</html>
