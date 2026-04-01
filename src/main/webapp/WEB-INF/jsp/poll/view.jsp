<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:layout title="poll.view.title">

    <!-- Back Link -->
    <a href="/poll/list" class="inline-block mb-4 text-blue-600 hover:text-blue-800 font-medium">
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
                            <label class="flex items-center p-4 border border-gray-300 rounded-lg hover:bg-gray-50 cursor-pointer">
                                <input type="radio"
                                       name="selectedOption"
                                       value="1"
                                       ${userVote == '1' ? 'checked' : ''}
                                       class="w-4 h-4 text-green-600 border-gray-300 focus:ring-green-500" />
                                <span class="ml-3 text-gray-700">${poll.option1}</span>
                                <span class="ml-auto text-gray-500 text-sm">
                                    (${voteCounts[1] != null ? voteCounts[1] : 0} <fmt:message key="poll.votes" />)
                                </span>
                            </label>
                        </c:if>
                        <c:if test="${not empty poll.option2}">
                            <label class="flex items-center p-4 border border-gray-300 rounded-lg hover:bg-gray-50 cursor-pointer">
                                <input type="radio"
                                       name="selectedOption"
                                       value="2"
                                       ${userVote == '2' ? 'checked' : ''}
                                       class="w-4 h-4 text-green-600 border-gray-300 focus:ring-green-500" />
                                <span class="ml-3 text-gray-700">${poll.option2}</span>
                                <span class="ml-auto text-gray-500 text-sm">
                                    (${voteCounts[2] != null ? voteCounts[2] : 0} <fmt:message key="poll.votes" />)
                                </span>
                            </label>
                        </c:if>
                        <c:if test="${not empty poll.option3}">
                            <label class="flex items-center p-4 border border-gray-300 rounded-lg hover:bg-gray-50 cursor-pointer">
                                <input type="radio"
                                       name="selectedOption"
                                       value="3"
                                       ${userVote == '3' ? 'checked' : ''}
                                       class="w-4 h-4 text-green-600 border-gray-300 focus:ring-green-500" />
                                <span class="ml-3 text-gray-700">${poll.option3}</span>
                                <span class="ml-auto text-gray-500 text-sm">
                                    (${voteCounts[3] != null ? voteCounts[3] : 0} <fmt:message key="poll.votes" />)
                                </span>
                            </label>
                        </c:if>
                        <c:if test="${not empty poll.option4}">
                            <label class="flex items-center p-4 border border-gray-300 rounded-lg hover:bg-gray-50 cursor-pointer">
                                <input type="radio"
                                       name="selectedOption"
                                       value="4"
                                       ${userVote == '4' ? 'checked' : ''}
                                       class="w-4 h-4 text-green-600 border-gray-300 focus:ring-green-500" />
                                <span class="ml-3 text-gray-700">${poll.option4}</span>
                                <span class="ml-auto text-gray-500 text-sm">
                                    (${voteCounts[4] != null ? voteCounts[4] : 0} <fmt:message key="poll.votes" />)
                                </span>
                            </label>
                        </c:if>
                        <c:if test="${not empty poll.option5}">
                            <label class="flex items-center p-4 border border-gray-300 rounded-lg hover:bg-gray-50 cursor-pointer">
                                <input type="radio"
                                       name="selectedOption"
                                       value="5"
                                       ${userVote == '5' ? 'checked' : ''}
                                       class="w-4 h-4 text-green-600 border-gray-300 focus:ring-green-500" />
                                <span class="ml-3 text-gray-700">${poll.option5}</span>
                                <span class="ml-auto text-gray-500 text-sm">
                                    (${voteCounts[5] != null ? voteCounts[5] : 0} <fmt:message key="poll.votes" />)
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
                            <span class="text-gray-500 text-sm">${voteCounts[1] != null ? voteCounts[1] : 0} votes</span>
                        </div>
                        <div class="w-full bg-gray-200 rounded-full h-2">
                            <div class="bg-green-600 h-2 rounded-full"
                                 style="width: ${poll.voteCount > 0 ? ((voteCounts[1] != null ? voteCounts[1] : 0) * 100 / poll.voteCount) : 0}%"></div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty poll.option2}">
                    <div class="p-4 border border-gray-300 rounded-lg">
                        <div class="flex justify-between mb-1">
                            <span class="text-gray-700">${poll.option2}</span>
                            <span class="text-gray-500 text-sm">${voteCounts[2] != null ? voteCounts[2] : 0} votes</span>
                        </div>
                        <div class="w-full bg-gray-200 rounded-full h-2">
                            <div class="bg-green-600 h-2 rounded-full"
                                 style="width: ${poll.voteCount > 0 ? ((voteCounts[2] != null ? voteCounts[2] : 0) * 100 / poll.voteCount) : 0}%"></div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty poll.option3}">
                    <div class="p-4 border border-gray-300 rounded-lg">
                        <div class="flex justify-between mb-1">
                            <span class="text-gray-700">${poll.option3}</span>
                            <span class="text-gray-500 text-sm">${voteCounts[3] != null ? voteCounts[3] : 0} votes</span>
                        </div>
                        <div class="w-full bg-gray-200 rounded-full h-2">
                            <div class="bg-green-600 h-2 rounded-full"
                                 style="width: ${poll.voteCount > 0 ? ((voteCounts[3] != null ? voteCounts[3] : 0) * 100 / poll.voteCount) : 0}%"></div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty poll.option4}">
                    <div class="p-4 border border-gray-300 rounded-lg">
                        <div class="flex justify-between mb-1">
                            <span class="text-gray-700">${poll.option4}</span>
                            <span class="text-gray-500 text-sm">${voteCounts[4] != null ? voteCounts[4] : 0} votes</span>
                        </div>
                        <div class="w-full bg-gray-200 rounded-full h-2">
                            <div class="bg-green-600 h-2 rounded-full"
                                 style="width: ${poll.voteCount > 0 ? ((voteCounts[4] != null ? voteCounts[4] : 0) * 100 / poll.voteCount) : 0}%"></div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty poll.option5}">
                    <div class="p-4 border border-gray-300 rounded-lg">
                        <div class="flex justify-between mb-1">
                            <span class="text-gray-700">${poll.option5}</span>
                            <span class="text-gray-500 text-sm">${voteCounts[5] != null ? voteCounts[5] : 0} votes</span>
                        </div>
                        <div class="w-full bg-gray-200 rounded-full h-2">
                            <div class="bg-green-600 h-2 rounded-full"
                                 style="width: ${poll.voteCount > 0 ? ((voteCounts[5] != null ? voteCounts[5] : 0) * 100 / poll.voteCount) : 0}%"></div>
                        </div>
                    </div>
                </c:if>
            </div>
        </sec:authorize>
    </div>

</t:layout>
