
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${requestScope.springLocale}" />
<fmt:setBundle basename="messages" />

<div class="flex flex-col h-full">
    <!-- List Header -->
    <div class="px-6 py-4 bg-gray-50 border-b border-gray-200 flex justify-between items-center">
        <h3 class="font-bold text-gray-800 uppercase tracking-wider text-sm">
            <c:choose>
                <c:when test="${param.type == 'lecture'}">
                    <fmt:message key="index.lectures" />
                </c:when>
                <c:otherwise>
                    <fmt:message key="index.polls" />
                </c:otherwise>
            </c:choose>

        </h3>
        <sec:authorize access="hasRole('TEACHER')">
            <a href="<c:url value='/${param.type}/create'/>"
               class="bg-blue-600 text-white p-1 rounded-full hover:bg-blue-700 transition-colors">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                </svg>
            </a>
        </sec:authorize>
    </div>

    <!-- Table Body -->
    <div class="overflow-x-auto">
        <table class="w-full text-sm text-left text-gray-500">
            <tbody class="divide-y divide-gray-100">
            <c:if test="${param.type == 'lecture'}">
                <c:forEach var="item" items="${lectures}">
                    <tr class="hover:bg-gray-50 transition-colors">
                        <td class="px-6 py-4 font-medium text-gray-900">${item.title}</td>
                        <td class="px-6 py-4 text-right">
                            <a href="/lecture/${item.lectureId}" class="text-blue-600 hover:underline">Details</a>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            <c:if test="${param.type == 'poll'}">
                <c:forEach var="item" items="${polls}">
                    <tr class="hover:bg-gray-50 transition-colors">
                        <td class="px-6 py-4 font-medium text-gray-900">${item.question}</td>
                        <td class="px-6 py-4 text-right">
                            <span class="text-xs text-gray-400 mr-2">${item.voteCount} votes</span>
                            <a href="/poll/${item.pollId}" class="text-green-600 hover:underline">Vote</a>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>

        <!-- Empty State -->
        <c:if test="${(param.type == 'lecture' && empty lectures) || (param.type == 'poll' && empty polls)}">
            <div class="p-10 text-center text-gray-400 italic">No data found.</div>
        </c:if>
    </div>
</div>

