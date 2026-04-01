<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:layout title="lecture.list.title">

    <div class="flex justify-between items-center mb-6">
        <h1 class="text-3xl font-bold text-gray-800">
            <fmt:message key="lecture.list.title" />
        </h1>
        <sec:authorize access="hasRole('TEACHER')">
            <a href="/lecture/create"
               class="bg-blue-600 text-white font-medium py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors">
                <fmt:message key="lecture.create.submit" />
            </a>
        </sec:authorize>
    </div>

    <c:choose>
        <c:when test="${empty lectures}">
            <p class="text-gray-500 italic"><fmt:message key="index.noLectures" /></p>
        </c:when>
        <c:otherwise>
            <div class="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                <c:forEach var="lecture" items="${lectures}">
                    <div class="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow p-6">
                        <h3 class="text-xl font-semibold text-gray-800 mb-2">
                            ${lecture.title}
                        </h3>
                        <p class="text-gray-600 text-sm mb-4 line-clamp-3">
                            ${lecture.summary}
                        </p>
                        <p class="text-gray-500 text-xs mb-4">
                            <fmt:message key="lecture.createdAt" />: ${lecture.createdAt}
                        </p>
                        <a href="/lecture/${lecture.lectureId}"
                           class="inline-block text-blue-600 hover:text-blue-800 font-medium">
                            <fmt:message key="index.viewDetails" /> &rarr;
                        </a>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

</t:layout>
