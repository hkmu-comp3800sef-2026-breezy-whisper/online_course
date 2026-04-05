<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setLocale value="${requestScope.springLocale}" />
<fmt:setBundle basename="messages" />

<t:layout title="user.comments.title">

    <h1 class="text-3xl font-bold text-gray-800 mb-6">
        <fmt:message key="user.comments.title" />
    </h1>

    <c:choose>
        <c:when test="${empty comments}">
            <p class="text-gray-500 italic"><fmt:message key="user.comments.noComments" /></p>
        </c:when>
        <c:otherwise>
            <div class="space-y-4">
                <c:forEach var="comment" items="${comments}">
                    <div class="bg-white rounded-lg shadow p-6">
                        <div class="flex justify-between items-start mb-2">
                            <div>
                                <span class="text-gray-500 text-sm">
                                    <fmt:message key="comment.on" /> ${comment.targetType} #${comment.targetId}
                                </span>
                                <span class="text-gray-500 text-sm ml-4">
                                    ${comment.createdAt}
                                </span>
                            </div>
                            <sec:authorize access="hasRole('TEACHER')">
                                <form action="/comment/${comment.commentId}/delete" method="post" class="inline">
                                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                    <button type="submit"
                                            class="text-red-600 hover:text-red-800 text-sm font-medium"
                                            onclick="return confirm('<fmt:message key="common.confirmDelete" />')">
                                        <fmt:message key="comment.delete" />
                                    </button>
                                </form>
                            </sec:authorize>
                        </div>
                        <p class="text-gray-700">${comment.content}</p>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

</t:layout>
