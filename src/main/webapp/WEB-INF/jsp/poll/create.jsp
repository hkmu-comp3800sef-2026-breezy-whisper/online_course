<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${requestScope.springLocale}" />
<fmt:setBundle basename="messages" />

<t:layout title="poll.create.title">

    <div class="max-w-2xl mx-auto">
        <a href="/" class="inline-block mb-4 text-blue-600 hover:text-blue-800 font-medium">
            &larr; <fmt:message key="common.back" />
        </a>

        <div class="bg-white rounded-lg shadow-md p-8">
            <h1 class="text-2xl font-bold text-gray-800 mb-8">
                <fmt:message key="poll.create.title" />
            </h1>

            <form action="/poll/create" method="post" data-validate>
                <input type="hidden" name="_csrf" value="${_csrf.token}" />

                <!-- Question -->
                <div class="mb-4">
                    <label for="question" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="poll.question" />
                    </label>
                    <input type="text"
                           id="question"
                           name="question"
                           required
                           data-validate="question"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                           placeholder="Enter poll question" />
                    <div id="question-error" class="text-red-500 text-sm mt-1 hidden"></div>
                </div>

                <!-- Options -->
                <div class="mb-4">
                    <label for="option1" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="poll.option1" />
                    </label>
                    <input type="text"
                           id="option1"
                           name="option1"
                           required
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                           placeholder="Option 1" />
                </div>

                <div class="mb-4">
                    <label for="option2" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="poll.option2" />
                    </label>
                    <input type="text"
                           id="option2"
                           name="option2"
                           required
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                           placeholder="Option 2" />
                </div>

                <div class="mb-4">
                    <label for="option3" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="poll.option3" />
                    </label>
                    <input type="text"
                           id="option3"
                           name="option3"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                           placeholder="Option 3 (optional)" />
                </div>

                <div class="mb-4">
                    <label for="option4" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="poll.option4" />
                    </label>
                    <input type="text"
                           id="option4"
                           name="option4"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                           placeholder="Option 4 (optional)" />
                </div>

                <div class="mb-4">
                    <label for="option5" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="poll.option5" />
                    </label>
                    <input type="text"
                           id="option5"
                           name="option5"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                           placeholder="Option 5 (optional)" />
                </div>

                <!-- Close Time -->
                <div class="mb-6">
                    <label for="closeTime" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="poll.closeTime" />
                    </label>
                    <input type="datetime-local"
                           id="closeTime"
                           name="closeTime"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>

                <!-- Submit -->
                <div class="flex gap-4">
                    <button type="submit"
                            class="bg-green-600 text-white font-medium py-2 px-4 rounded-lg hover:bg-green-700 transition-colors">
                        <fmt:message key="common.submit" />
                    </button>
                    <a href="/"
                       class="bg-gray-300 text-gray-700 font-medium py-2 px-4 rounded-lg hover:bg-gray-400 transition-colors">
                        <fmt:message key="common.cancel" />
                    </a>
                </div>
            </form>
        </div>
    </div>

</t:layout>
