<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${requestScope.springLocale}" />
<fmt:setBundle basename="messages" />

<t:layout title="admin.users.title">

    <h1 class="text-3xl font-bold text-gray-800 mb-6">
        <fmt:message key="admin.users.title" />
    </h1>

    <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
                <tr>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        <fmt:message key="admin.users.username" />
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        <fmt:message key="admin.users.fullName" />
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        <fmt:message key="admin.users.email" />
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        <fmt:message key="admin.users.role" />
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        <fmt:message key="admin.users.status" />
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        <fmt:message key="admin.users.actions" />
                    </th>
                </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
                <c:choose>
                    <c:when test="${empty users}">
                        <tr>
                            <td colspan="6" class="px-6 py-4 text-center text-gray-500">
                                <fmt:message key="admin.users.noUsers" />
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="user" items="${users}">
                            <tr>
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    ${user.username}
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    ${user.fullName}
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    ${user.email}
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm">
                                    <c:choose>
                                        <c:when test="${user.role == 1}">
                                            <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-purple-100 text-purple-800">
                                                <fmt:message key="user.profile.teacher" />
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800">
                                                <fmt:message key="user.profile.student" />
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm">
                                    <c:choose>
                                        <c:when test="${user.status == 0}">
                                            <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                                                <fmt:message key="user.profile.active" />
                                            </span>
                                        </c:when>
                                        <c:when test="${user.status == 1}">
                                            <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-yellow-100 text-yellow-800">
                                                <fmt:message key="user.profile.pending" />
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800">
                                                <fmt:message key="user.profile.disabled" />
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                    <c:choose>
                                        <c:when test="${user.status == 0}">
                                            <form action="/admin/users/${user.username}/disable" method="post" class="inline">
                                                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                                <button type="submit" class="text-red-600 hover:text-red-900 mr-3">
                                                    <fmt:message key="admin.users.disable" />
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <form action="/admin/users/${user.username}/enable" method="post" class="inline">
                                                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                                <button type="submit" class="text-green-600 hover:text-green-900 mr-3">
                                                    <fmt:message key="admin.users.enable" />
                                                </button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

</t:layout>
