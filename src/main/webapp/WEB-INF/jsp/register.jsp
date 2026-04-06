<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${requestScope.springLocale}" />
<fmt:setBundle basename="messages" />

<t:layout title="register.title">

    <div class="max-w-md mx-auto">
        <div class="bg-white rounded-lg shadow-md p-8">

    <h1 class="text-2xl font-bold text-center text-gray-800 mb-8">
        <c:choose>
            <c:when test="${adminRegister}">
                <fmt:message key="admin.users.add" />
            </c:when>
            <c:otherwise>
                <fmt:message key="register.title" />
            </c:otherwise>
        </c:choose>
    </h1>


            <!-- Error Message -->
            <c:if test="${param.error != null}">
                <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
                    <fmt:message key="register.error" />
                </div>
            </c:if>

            <form action="/register" method="post" data-validate>

                <!-- CSRF Token -->
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <!-- Admin Register Flag -->
                <c:if test="${adminRegister}">
                    <input type="hidden" name="adminRegister" value="true" />
                </c:if>


                <!-- Username -->
                <div class="mb-4">
                    <label for="username" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="register.username" />
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
                        <fmt:message key="register.password" />
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

                <!-- Full Name -->
                <div class="mb-4">
                    <label for="fullName" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="register.fullName" />
                    </label>
                    <input type="text"
                           id="fullName"
                           name="fullName"
                           required
                           data-validate="fullName"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                           placeholder="Enter your full name" />
                    <div id="fullName-error" class="text-red-500 text-sm mt-1 hidden"></div>
                </div>

                <!-- Email -->
                <div class="mb-4">
                    <label for="email" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="register.email" />
                    </label>
                    <input type="email"
                           id="email"
                           name="email"
                           required
                           data-validate="email"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                           placeholder="Enter your email" />
                    <div id="email-error" class="text-red-500 text-sm mt-1 hidden"></div>
                </div>

                <!-- Phone Number -->
                <div class="mb-4">
                    <label for="phoneNumber" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="register.phoneNumber" />
                    </label>
                    <input type="tel"
                           id="phoneNumber"
                           name="phoneNumber"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                           placeholder="Enter your phone number" />
                </div>

                <!-- Role -->
                <div class="mb-6">
                    <label for="role" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="register.role" />
                    </label>
                    <select id="role"
                            name="role"
                            required
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="ROLE_STUDENT"><fmt:message key="register.roleStudent" /></option>
                        <option value="ROLE_TEACHER"><fmt:message key="register.roleTeacher" /></option>
                    </select>
                </div>

                <!-- Submit -->
                <button type="submit"
                        class="w-full bg-blue-600 text-white font-medium py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors">
                        <c:choose>
                            <c:when test="${adminRegister}">
                                <fmt:message key="admin.users.add" />
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="register.submit" />
                            </c:otherwise>
                        </c:choose>
                </button>
            </form>

            <!-- Login Link -->
            
            <c:if test="${empty adminRegister}">
                <p class="text-center mt-6 text-gray-600">
                    <fmt:message key="login.register" />
                    <a href="/login" class="text-blue-600 hover:underline">Login</a>
                </p>
            </c:if>
        </div>
    </div>

</t:layout>
