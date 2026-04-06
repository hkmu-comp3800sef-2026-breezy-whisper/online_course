<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${requestScope.springLocale}" />
<fmt:setBundle basename="messages" />


<t:layout title="login.title">

    <div class="max-w-md mx-auto">
        <div class="bg-white rounded-lg shadow-md p-8">
            <h1 class="text-2xl font-bold text-center text-gray-800 mb-8">
                <fmt:message key="login.title" />
            </h1>

            <!-- Error Message -->
            <c:if test="${param.error != null}">
                <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
                    <fmt:message key="login.error" />
                </div>
            </c:if>

            <!-- Logout Message -->
            <c:if test="${param.logout != null}">
                <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">
                    <fmt:message key="login.logoutSuccess" />
                </div>
            </c:if>

            <!-- Registered Message -->
            <c:if test="${param.registered != null}">
                <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">
                    <fmt:message key="register.success" />
                </div>
            </c:if>

            <form action="/perform-login" method="post" data-validate>
                <!-- CSRF Token (Spring Security) -->
                <input type="hidden" name="_csrf" value="${_csrf.token}" />

                <!-- Username -->
                <div class="mb-4">
                    <label for="username" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="login.username" />
                    </label>
                    <input type="text"
                           id="username"
                           name="username"
                           required
                           autofocus
                           data-validate="username"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                           placeholder="Enter your username" />
                    <div id="username-error" class="text-red-500 text-sm mt-1 hidden"></div>
                </div>

                <!-- Password -->
                <div class="mb-4">
                    <label for="password" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="login.password" />
                    </label>
                    <input type="password"
                           id="password"
                           name="password"
                           required
                           data-validate="password"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                           placeholder="Enter your password" />
                    <div id="password-error" class="text-red-500 text-sm mt-1 hidden"></div>
                </div>

                <!-- Remember Me -->
                <div class="mb-6">
                    <label class="flex items-center">
                        <input type="checkbox"
                               name="remember-me"
                               class="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500" />
                        <span class="ml-2 text-gray-700">
                            <fmt:message key="login.rememberMe" />
                        </span>
                    </label>
                </div>

                <!-- Submit -->
                <button type="submit"
                        class="w-full bg-blue-600 text-white font-medium py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors">
                    <fmt:message key="login.submit" />
                </button>
            </form>

            <!-- Register Link -->
            <p class="text-center mt-6 text-gray-600">
                <fmt:message key="login.register" />
                <a href="/register" class="text-blue-600 hover:underline">
                    <fmt:message key="register.title" />
                </a>
            </p>
        </div>
    </div>

</t:layout>
