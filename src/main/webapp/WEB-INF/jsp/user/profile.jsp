<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${requestScope.springLocale}" />
<fmt:setBundle basename="messages" />

<t:layout title="user.profile.title">

    <div class="max-w-2xl mx-auto">
        <div class="bg-white rounded-lg shadow-md p-8">
            <h1 class="text-2xl font-bold text-gray-800 mb-8">
                <fmt:message key="user.profile.title" />
            </h1>

            <!-- Success Message -->
            <c:if test="${param.updated != null}">
                <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-6">
                    <fmt:message key="user.profile.update.success" />
                </div>
            </c:if>

            <form action="/user/profile" method="post" data-validate>
                <input type="hidden" name="_csrf" value="${_csrf.token}" />

                <!-- Username (read-only) -->
                <div class="mb-4">
                    <label for="username" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="user.profile.username" />
                    </label>
                    <input type="text"
                           id="username"
                           value="${user.username}"
                           readonly
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-100 text-gray-600" />
                </div>

                <!-- Full Name -->
                <div class="mb-4">
                    <label for="fullName" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="user.profile.fullName" />
                    </label>
                    <input type="text"
                           id="fullName"
                           name="fullName"
                           required
                           value="${user.fullName}"
                           data-validate="fullName"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
                    <div id="fullName-error" class="text-red-500 text-sm mt-1 hidden"></div>
                </div>

                <!-- Email -->
                <div class="mb-4">
                    <label for="email" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="user.profile.email" />
                    </label>
                    <input type="email"
                           id="email"
                           name="email"
                           required
                           value="${user.email}"
                           data-validate="email"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
                    <div id="email-error" class="text-red-500 text-sm mt-1 hidden"></div>
                </div>

                <!-- Phone Number -->
                <div class="mb-4">
                    <label for="phoneNumber" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="user.profile.phoneNumber" />
                    </label>
                    <input type="tel"
                           id="phoneNumber"
                           name="phoneNumber"
                           value="${user.phoneNumber}"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>

                <!-- Role (read-only) -->
                <div class="mb-4">
                    <label class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="user.profile.role" />
                    </label>
                    <input type="text"
                           value="<fmt:message key="${user.role == 1 ? 'user.profile.teacher' : 'user.profile.student'}" />"
                           readonly
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-100 text-gray-600" />
                </div>

                <!-- Status (read-only) -->
                <div class="mb-6">
                    <label class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="user.profile.status" />
                    </label>
                    <input type="text"
                           value="<fmt:message key="${user.status == 0 ? 'user.profile.active' : user.status == 1 ? 'user.profile.pending' : 'user.profile.disabled'}" />"
                           readonly
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-100 text-gray-600" />
                </div>

                <!-- Submit -->
                <button type="submit"
                        class="bg-blue-600 text-white font-medium py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors">
                    <fmt:message key="user.profile.update.submit" />
                </button>
            </form>
        </div>
    </div>

</t:layout>
