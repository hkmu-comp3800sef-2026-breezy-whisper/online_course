<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<t:layout title="index.title">

    <!-- Welcome Section -->
    <div class="text-center mb-10">
        <h1 class="text-4xl font-bold text-gray-800 mb-4">
            <fmt:message key="index.welcome" />
        </h1>
        <p class="text-gray-600">COMP 3800SEF/3820SEF/S380F Online Course</p>
    </div>

    <!-- Lectures Section -->
    <section class="mb-12">
        <h2 class="text-2xl font-bold text-gray-800 mb-6 pb-2 border-b-2 border-blue-500">
            <fmt:message key="index.lectures" />
        </h2>

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
                            <a href="/lecture/${lecture.lectureId}"
                               class="inline-block text-blue-600 hover:text-blue-800 font-medium">
                                <fmt:message key="index.viewDetails" /> &rarr;
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <!-- Polls Section -->
    <section>
        <h2 class="text-2xl font-bold text-gray-800 mb-6 pb-2 border-b-2 border-green-500">
            <fmt:message key="index.polls" />
        </h2>

        <c:choose>
            <c:when test="${empty polls}">
                <p class="text-gray-500 italic"><fmt:message key="index.noPolls" /></p>
            </c:when>
            <c:otherwise>
                <div class="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                    <c:forEach var="poll" items="${polls}">
                        <div class="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow p-6">
                            <h3 class="text-xl font-semibold text-gray-800 mb-2">
                                ${poll.question}
                            </h3>
                            <p class="text-gray-500 text-sm mb-4">
                                <fmt:message key="poll.votes" />: ${poll.voteCount}
                            </p>
                            <a href="/poll/${poll.pollId}"
                               class="inline-block text-green-600 hover:text-green-800 font-medium">
                                <fmt:message key="index.viewDetails" /> &rarr;
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

</t:layout>
