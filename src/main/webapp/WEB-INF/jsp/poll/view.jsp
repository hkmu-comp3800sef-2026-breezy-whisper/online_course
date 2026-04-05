<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${requestScope.springLocale}" />
<fmt:setBundle basename="messages" />
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:layout title="poll.view.title">

    <!-- Back Link -->
    <a href="/" class="inline-block mb-4 text-blue-600 hover:text-blue-800 font-medium">
        &larr; <fmt:message key="common.back" />
    </a>

    <!-- Poll Details -->
    <div class="bg-white rounded-lg shadow-md p-8 mb-8">
        <h1 class="text-3xl font-bold text-gray-800 mb-4">
            ${poll.question}
        </h1>

        <c:choose>
            <c:when test="${poll.closed}">
                <span class="inline-block bg-gray-200 text-gray-600 text-sm font-medium py-1 px-3 rounded mb-4">
                    <fmt:message key="poll.closed" />
                </span>
            </c:when>
            <c:otherwise>
                <span class="inline-block bg-green-100 text-green-600 text-sm font-medium py-1 px-3 rounded mb-4">
                    <fmt:message key="poll.open" />
                </span>
            </c:otherwise>
        </c:choose>

        <p class="text-gray-600 mb-6">
            <fmt:message key="poll.votes" />: ${poll.voteCount}
        </p>

        <sec:authorize access="hasRole('TEACHER')">
            <div class="flex gap-4 mb-6">
                <form action="/poll/${poll.pollId}/delete" method="post" class="inline"
                      onsubmit="return confirm('<fmt:message key="common.confirmDelete" />')">
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                    <button type="submit"
                            class="bg-red-600 text-white font-medium py-2 px-4 rounded-lg hover:bg-red-700 transition-colors">
                        <fmt:message key="poll.delete.submit" />
                    </button>
                </form>
            </div>
        </sec:authorize>

        <!-- Voting Options -->
        <sec:authorize access="isAuthenticated()">
            <c:if test="${not poll.closed}">
                <form action="/poll/${poll.pollId}/vote" method="post">
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />

                    <div class="space-y-3 mb-6">
                        <c:if test="${not empty poll.option1}">
                            <label class="relative flex cursor-pointer items-center overflow-hidden rounded-lg border border-gray-300 p-4">
                                <span class="absolute inset-y-0 left-0 bg-red-300/70"
                                      style="width: ${poll.voteCount > 0 ? ((voteCounts[1] != null ? voteCounts[1] : 0) * 100 / poll.voteCount) : 0}%"></span>
                                <input type="radio"
                                       name="selectedOption"
                                       value="1"
                                       ${userVote == '1' ? 'checked' : ''}
                                       class="relative z-10 h-4 w-4 border-gray-300 text-red-500 focus:ring-red-400" />
                                <span class="relative z-10 ml-3 text-gray-700">${poll.option1}</span>
                                <span class="relative z-10 ml-auto text-sm text-gray-600">
                                    (${not empty voteCounts["1"] ? voteCounts["1"] : 0} <fmt:message key="poll.votes" />)
                                </span>
                            </label>
                        </c:if>
                        <c:if test="${not empty poll.option2}">
                            <label class="relative flex cursor-pointer items-center overflow-hidden rounded-lg border border-gray-300 p-4">
                                <span class="absolute inset-y-0 left-0 bg-orange-300/70"
                                      style="width: ${poll.voteCount > 0 ? ((voteCounts[2] != null ? voteCounts[2] : 0) * 100 / poll.voteCount) : 0}%"></span>
                                <input type="radio"
                                       name="selectedOption"
                                       value="2"
                                       ${userVote == '2' ? 'checked' : ''}
                                       class="relative z-10 h-4 w-4 border-gray-300 text-orange-500 focus:ring-orange-400" />
                                <span class="relative z-10 ml-3 text-gray-700">${poll.option2}</span>
                                <span class="relative z-10 ml-auto text-sm text-gray-600">
                                    (${not empty voteCounts["2"] ? voteCounts["2"] : 0} <fmt:message key="poll.votes" />)
                                </span>
                            </label>
                        </c:if>
                        <c:if test="${not empty poll.option3}">
                            <label class="relative flex cursor-pointer items-center overflow-hidden rounded-lg border border-gray-300 p-4">
                                <span class="absolute inset-y-0 left-0 bg-amber-300/70"
                                      style="width: ${poll.voteCount > 0 ? ((voteCounts[3] != null ? voteCounts[3] : 0) * 100 / poll.voteCount) : 0}%"></span>
                                <input type="radio"
                                       name="selectedOption"
                                       value="3"
                                       ${userVote == '3' ? 'checked' : ''}
                                       class="relative z-10 h-4 w-4 border-gray-300 text-amber-500 focus:ring-amber-400" />
                                <span class="relative z-10 ml-3 text-gray-700">${poll.option3}</span>
                                <span class="relative z-10 ml-auto text-sm text-gray-600">
                                    (${not empty voteCounts["3"] ? voteCounts["3"] : 0} <fmt:message key="poll.votes" />)
                                </span>
                            </label>
                        </c:if>
                        <c:if test="${not empty poll.option4}">
                            <label class="relative flex cursor-pointer items-center overflow-hidden rounded-lg border border-gray-300 p-4">
                                <span class="absolute inset-y-0 left-0 bg-rose-300/70"
                                      style="width: ${poll.voteCount > 0 ? ((voteCounts[4] != null ? voteCounts[4] : 0) * 100 / poll.voteCount) : 0}%"></span>
                                <input type="radio"
                                       name="selectedOption"
                                       value="4"
                                       ${userVote == '4' ? 'checked' : ''}
                                       class="relative z-10 h-4 w-4 border-gray-300 text-rose-500 focus:ring-rose-400" />
                                <span class="relative z-10 ml-3 text-gray-700">${poll.option4}</span>
                                <span class="relative z-10 ml-auto text-sm text-gray-600">
                                    (${not empty voteCounts["4"] ? voteCounts["4"] : 0} <fmt:message key="poll.votes" />)
                                </span>
                            </label>
                        </c:if>
                        <c:if test="${not empty poll.option5}">
                            <label class="relative flex cursor-pointer items-center overflow-hidden rounded-lg border border-gray-300 p-4">
                                <span class="absolute inset-y-0 left-0 bg-pink-300/70"
                                      style="width: ${poll.voteCount > 0 ? ((voteCounts[5] != null ? voteCounts[5] : 0) * 100 / poll.voteCount) : 0}%"></span>
                                <input type="radio"
                                       name="selectedOption"
                                       value="5"
                                       ${userVote == '5' ? 'checked' : ''}
                                       class="relative z-10 h-4 w-4 border-gray-300 text-pink-500 focus:ring-pink-400" />
                                <span class="relative z-10 ml-3 text-gray-700">${poll.option5}</span>
                                <span class="relative z-10 ml-auto text-sm text-gray-600">
                                    (${not empty voteCounts["5"] ? voteCounts["5"] : 0} <fmt:message key="poll.votes" />)
                                </span>
                            </label>
                        </c:if>
                    </div>

                    <c:choose>
                        <c:when test="${userVote != null}">
                            <button type="submit"
                                    class="bg-green-600 text-white font-medium py-2 px-4 rounded-lg hover:bg-green-700 transition-colors">
                                <fmt:message key="poll.changeVote" />
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button type="submit"
                                    class="bg-green-600 text-white font-medium py-2 px-4 rounded-lg hover:bg-green-700 transition-colors">
                                <fmt:message key="poll.submitVote" />
                            </button>
                        </c:otherwise>
                    </c:choose>
                </form>
            </c:if>
        </sec:authorize>

        <!-- Results View (for non-authenticated or closed polls) -->
        <sec:authorize access="isAnonymous()">
            <div class="space-y-3">
                <c:if test="${not empty poll.option1}">
                    <div class="p-4 border border-gray-300 rounded-lg">
                        <div class="flex justify-between mb-1">
                            <span class="text-gray-700">${poll.option1}</span>
                            <span class="text-gray-500 text-sm">${not empty voteCounts["1"] ? voteCounts["1"] : 0} votes</span>
                        </div>
                        <div class="w-full bg-gray-200 rounded-full h-2">
                            <div class="bg-red-400 h-2 rounded-full"
                                 style="width: ${poll.voteCount > 0 ? ((voteCounts[1] != null ? voteCounts[1] : 0) * 100 / poll.voteCount) : 0}%"></div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty poll.option2}">
                    <div class="p-4 border border-gray-300 rounded-lg">
                        <div class="flex justify-between mb-1">
                            <span class="text-gray-700">${poll.option2}</span>
                            <span class="text-gray-500 text-sm">${not empty voteCounts["2"] ? voteCounts["2"] : 0} votes</span>
                        </div>
                        <div class="w-full bg-gray-200 rounded-full h-2">
                            <div class="bg-orange-400 h-2 rounded-full"
                                 style="width: ${poll.voteCount > 0 ? ((voteCounts[2] != null ? voteCounts[2] : 0) * 100 / poll.voteCount) : 0}%"></div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty poll.option3}">
                    <div class="p-4 border border-gray-300 rounded-lg">
                        <div class="flex justify-between mb-1">
                            <span class="text-gray-700">${poll.option3}</span>
                            <span class="text-gray-500 text-sm">${not empty voteCounts["3"] ? voteCounts["3"] : 0} votes</span>
                        </div>
                        <div class="w-full bg-gray-200 rounded-full h-2">
                            <div class="bg-amber-400 h-2 rounded-full"
                                 style="width: ${poll.voteCount > 0 ? ((voteCounts[3] != null ? voteCounts[3] : 0) * 100 / poll.voteCount) : 0}%"></div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty poll.option4}">
                    <div class="p-4 border border-gray-300 rounded-lg">
                        <div class="flex justify-between mb-1">
                            <span class="text-gray-700">${poll.option4}</span>
                            <span class="text-gray-500 text-sm">${not empty voteCounts["4"] ? voteCounts["4"] : 0} votes</span>
                        </div>
                        <div class="w-full bg-gray-200 rounded-full h-2">
                            <div class="bg-rose-400 h-2 rounded-full"
                                 style="width: ${poll.voteCount > 0 ? ((voteCounts[4] != null ? voteCounts[4] : 0) * 100 / poll.voteCount) : 0}%"></div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty poll.option5}">
                    <div class="p-4 border border-gray-300 rounded-lg">
                        <div class="flex justify-between mb-1">
                            <span class="text-gray-700">${poll.option5}</span>
                            <span class="text-gray-500 text-sm">${not empty voteCounts["5"] ? voteCounts["5"] : 0} votes</span>
                        </div>
                        <div class="w-full bg-gray-200 rounded-full h-2">
                            <div class="bg-pink-400 h-2 rounded-full"
                                 style="width: ${poll.voteCount > 0 ? ((voteCounts[5] != null ? voteCounts[5] : 0) * 100 / poll.voteCount) : 0}%"></div>
                        </div>
                    </div>
                </c:if>
            </div>
        </sec:authorize>
    </div>

    <!-- Comments Section -->
    <section>
        <h2 class="text-2xl font-bold text-gray-800 mb-6 pb-2 border-b-2 border-green-500">
            <fmt:message key="lecture.comments" />
        </h2>

        <!-- Comment Form -->
        <sec:authorize access="isAuthenticated()">
            <form action="/comment/create" method="post" class="mb-8">
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <input type="hidden" name="targetId" value="${poll.pollId}" />
                <input type="hidden" name="targetType" value="POLL" />
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
                                <sec:authorize access="hasRole('TEACHER')">
                                    <form action="/comment/${comment.commentId}/delete" method="post" class="inline">
                                        <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                        <button type="submit"
                                                class="text-red-600 hover:text-red-800 text-sm font-medium">
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
    </section>

</t:layout>
