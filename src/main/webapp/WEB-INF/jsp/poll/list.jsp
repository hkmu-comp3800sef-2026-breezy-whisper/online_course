<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${requestScope.springLocale}" />
<fmt:setBundle basename="messages" />
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:layout title="poll.list.title">

    <div class="flex justify-between items-center mb-6">
        <h1 class="text-3xl font-bold text-gray-800">
            <fmt:message key="poll.list.title" />
        </h1>
        <sec:authorize access="hasRole('TEACHER')">
            <a href="/poll/create"
               class="bg-green-600 text-white font-medium py-2 px-4 rounded-lg hover:bg-green-700 transition-colors">
                <fmt:message key="poll.create.title" />
            </a>
        </sec:authorize>
    </div>

    <c:choose>
        <c:when test="${empty polls}">
            <p class="text-gray-500 italic"><fmt:message key="poll.noPolls" /></p>
        </c:when>
        <c:otherwise>
            <div class="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                <c:forEach var="poll" items="${polls}">
                    <div class="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow p-6">
                        <h3 class="text-xl font-semibold text-gray-800 mb-2">
                            ${poll.question}
                        </h3>
                        <p class="text-gray-500 text-sm mb-2">
                            <fmt:message key="poll.votes" />: ${poll.voteCount}
                        </p>
                        <c:choose>
                            <c:when test="${poll.closed}">
                                <span class="inline-block bg-gray-200 text-gray-600 text-sm font-medium py-1 px-3 rounded mb-2">
                                    <fmt:message key="poll.closed" />
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="inline-block bg-green-100 text-green-600 text-sm font-medium py-1 px-3 rounded mb-2">
                                    <fmt:message key="poll.open" />
                                </span>
                            </c:otherwise>
                        </c:choose>
                        <a href="/poll/${poll.pollId}"
                           class="block mt-2 text-green-600 hover:text-green-800 font-medium">
                            <fmt:message key="index.viewDetails" /> &rarr;
                        </a>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

</t:layout>
