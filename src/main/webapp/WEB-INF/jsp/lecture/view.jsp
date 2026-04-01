<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:layout title="lecture.view.title">

    <!-- Back Link -->
    <a href="/lecture/list" class="inline-block mb-4 text-blue-600 hover:text-blue-800 font-medium">
        &larr; <fmt:message key="lecture.back" />
    </a>

    <!-- Lecture Details -->
    <div class="bg-white rounded-lg shadow-md p-8 mb-8">
        <h1 class="text-3xl font-bold text-gray-800 mb-4">
            ${lecture.title}
        </h1>
        <p class="text-gray-600 mb-4">
            <fmt:message key="lecture.createdAt" />: ${lecture.createdAt}
        </p>
        <p class="text-gray-700 mb-6">
            ${lecture.summary}
        </p>

        <sec:authorize access="hasRole('TEACHER')">
            <div class="flex gap-4">
                <a href="/lecture/${lecture.lectureId}/edit"
                   class="bg-blue-600 text-white font-medium py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors">
                    <fmt:message key="common.edit" />
                </a>
                <form action="/lecture/${lecture.lectureId}/delete" method="post" class="inline"
                      onsubmit="return confirm('<fmt:message key="common.confirmDelete" />')">
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                    <button type="submit"
                            class="bg-red-600 text-white font-medium py-2 px-4 rounded-lg hover:bg-red-700 transition-colors">
                        <fmt:message key="lecture.delete.submit" />
                    </button>
                </form>
            </div>
        </sec:authorize>
    </div>

    <!-- Course Materials Section -->
    <section class="mb-8">
        <h2 class="text-2xl font-bold text-gray-800 mb-6 pb-2 border-b-2 border-blue-500">
            <fmt:message key="lecture.materials" />
        </h2>

        <c:choose>
            <c:when test="${empty lecture.materials}">
                <p class="text-gray-500 italic"><fmt:message key="lecture.noMaterials" /></p>
            </c:when>
            <c:otherwise>
                <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
                    <c:forEach var="material" items="${lecture.materials}">
                        <div class="bg-white rounded-lg shadow p-4 flex items-center justify-between">
                            <div>
                                <p class="font-medium text-gray-800">${material.fileName}</p>
                                <p class="text-gray-500 text-sm">
                                    <fmt:formatNumber value="${material.fileSize / 1024}" pattern="#,##0.##" /> KB
                                </p>
                            </div>
                            <a href="/lecture/${lecture.lectureId}/material/${material.materialId}/download"
                               class="text-blue-600 hover:text-blue-800 font-medium">
                                <fmt:message key="lecture.download" />
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <!-- Comments Section -->
    <section>
        <h2 class="text-2xl font-bold text-gray-800 mb-6 pb-2 border-b-2 border-green-500">
            <fmt:message key="lecture.comments" />
        </h2>

        <!-- Comment Form -->
        <sec:authorize access="isAuthenticated()">
            <form action="/comment/lecture/${lecture.lectureId}" method="post" class="mb-8">
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <div class="bg-white rounded-lg shadow p-4">
                    <label for="content" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="comment.content" />
                    </label>
                    <textarea id="content"
                              name="content"
                              required
                              rows="3"
                              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 mb-4"
                              placeholder="Write your comment here..."></textarea>
                    <button type="submit"
                            class="bg-green-600 text-white font-medium py-2 px-4 rounded-lg hover:bg-green-700 transition-colors">
                        <fmt:message key="comment.submit" />
                    </button>
                </div>
            </form>
        </sec:authorize>

        <!-- Comments List -->
        <c:choose>
            <c:when test="${empty comments}">
                <p class="text-gray-500 italic"><fmt:message key="lecture.noComments" /></p>
            </c:when>
            <c:otherwise>
                <div class="space-y-4">
                    <c:forEach var="comment" items="${comments}">
                        <div class="bg-white rounded-lg shadow p-4">
                            <div class="flex justify-between items-start mb-2">
                                <div>
                                    <span class="font-medium text-gray-800">${comment.username}</span>
                                    <span class="text-gray-500 text-sm ml-2">
                                        <fmt:message key="comment.on" /> ${comment.createdAt}
                                    </span>
                                </div>
                                <c:if test="${comment.username == pageContext.request.userPrincipal.name}">
                                    <form action="/comment/${comment.commentId}/delete" method="post" class="inline">
                                        <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                        <button type="submit"
                                                class="text-red-600 hover:text-red-800 text-sm font-medium">
                                            <fmt:message key="comment.delete" />
                                        </button>
                                    </form>
                                </c:if>
                            </div>
                            <p class="text-gray-700">${comment.content}</p>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

</t:layout>
