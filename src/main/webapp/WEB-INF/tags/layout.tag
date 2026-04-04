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
<body class="grid min-h-[100dvh] grid-cols-[minmax(0,1fr)] grid-rows-[auto_1fr_auto]">
    <header class="w-full z-20 top-0 start-0">
        <!-- Top Bar: Logo & Auth Action (Login/Logout) -->
        <nav class="bg-neutral-primary">
            <div class="flex flex-wrap justify-between items-center w-full px-6 py-4">
                <a href="/" class="flex items-center space-x-3 rtl:space-x-reverse">
                <span class="self-center text-xl text-heading font-semibold whitespace-nowrap">
                    <fmt:message key="index.title" />
                </span>
                </a>
                <div class="flex items-center space-x-6 rtl:space-x-reverse">
                    <!-- Anonymous: Show Login/Register -->
                    <sec:authorize access="isAnonymous()">
                        <a href="/login" class="text-sm font-medium text-fg-brand border-b-4 border-transparent hover:border-blue-700 transition-all">
                            <fmt:message key="nav.login" />
                        </a>
                        <a href="/register" class="text-sm font-medium text-fg-brand border-b-4 border-transparent hover:border-blue-700 transition-all">
                            <fmt:message key="nav.register" />
                        </a>
                    </sec:authorize>

                    <!-- Authenticated: Show Username & Logout -->
                    <sec:authorize access="isAuthenticated()">
                    <span class="text-sm text-gray-500">
                        <fmt:message key="nav.loggedInAs" />: <sec:authentication property="name" />
                    </span>
                        <form action="/logout" method="post" class="inline">
                            <input type="hidden" name="_csrf" value="${_csrf.token}" />
                            <button type="submit" class="text-sm font-medium text-red-600 border-b-4 border-transparent hover:border-red-600 transition-all">
                                <fmt:message key="nav.logout" />
                            </button>
                        </form>
                    </sec:authorize>

                    <!-- Language Switcher -->
                    <div class="text-xs space-x-2 border-l pl-4 border-gray-300">
                        <a href="?lang=en" class="hover:underline">EN</a>
                        <span class="text-gray-300">|</span>
                        <a href="?lang=zh" class="hover:underline">中文</a>
                    </div>
                </div>
            </div>
        </nav>

        <!-- Bottom Bar: Navigation Links -->
        <nav class="bg-neutral-secondary-soft border-y border-default">
            <div class="w-full px-6 py-3">
                <div class="flex items-center">
                    <ul class="flex flex-row font-medium mt-0 space-x-8 rtl:space-x-reverse text-sm">
                        <li>
                            <a href="/" class="text-heading border-b-4 border-transparent hover:border-blue-700 transition-all">
                                <fmt:message key="nav.home" />
                            </a>
                        </li>

                        <sec:authorize access="isAuthenticated()">
                            <li>
                                <a href="/user/comments" class="text-heading border-b-4 border-transparent hover:border-blue-700 transition-all">
                                    <fmt:message key="nav.myComments" />
                                </a>
                            </li>
                            <li>
                                <a href="/user/profile" class="text-heading border-b-4 border-transparent hover:border-blue-700 transition-all">
                                    <fmt:message key="nav.profile" />
                                </a>
                            </li>

                            <!-- Teacher Only -->
                            <sec:authorize access="hasRole('TEACHER')">
                                <li>
                                    <a href="/admin/users" class="text-heading border-b-4 border-transparent hover:border-blue-700 transition-all">
                                        <fmt:message key="nav.userManagement" />
                                    </a>
                                </li>
                            </sec:authorize>
                        </sec:authorize>
                    </ul>
                </div>
            </div>
        </nav>
    </header>


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
