<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<t:layout title="lecture.edit.title">

    <div class="max-w-2xl mx-auto">
        <a href="/lecture/list" class="inline-block mb-4 text-blue-600 hover:text-blue-800 font-medium">
            &larr; <fmt:message key="lecture.back" />
        </a>

        <div class="bg-white rounded-lg shadow-md p-8">
            <h1 class="text-2xl font-bold text-gray-800 mb-8">
                <fmt:message key="lecture.edit.title" />
            </h1>

            <form action="/lecture/${lecture.lectureId}/edit" method="post" data-validate>
                <input type="hidden" name="_csrf" value="${_csrf.token}" />

                <!-- Title -->
                <div class="mb-4">
                    <label for="title" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="lecture.title" />
                    </label>
                    <input type="text"
                           id="title"
                           name="title"
                           required
                           value="${lecture.title}"
                           data-validate="title"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
                    <div id="title-error" class="text-red-500 text-sm mt-1 hidden"></div>
                </div>

                <!-- Summary -->
                <div class="mb-4">
                    <label for="summary" class="block text-gray-700 font-medium mb-2">
                        <fmt:message key="lecture.summary" />
                    </label>
                    <textarea id="summary"
                              name="summary"
                              required
                              rows="5"
                              data-validate="summary"
                              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">${lecture.summary}</textarea>
                    <div id="summary-error" class="text-red-500 text-sm mt-1 hidden"></div>
                </div>

                <!-- Submit -->
                <div class="flex gap-4">
                    <button type="submit"
                            class="bg-blue-600 text-white font-medium py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors">
                        <fmt:message key="lecture.edit.submit" />
                    </button>
                    <a href="/lecture/list"
                       class="bg-gray-300 text-gray-700 font-medium py-2 px-4 rounded-lg hover:bg-gray-400 transition-colors">
                        <fmt:message key="common.cancel" />
                    </a>
                </div>
            </form>
        </div>
    </div>

</t:layout>
