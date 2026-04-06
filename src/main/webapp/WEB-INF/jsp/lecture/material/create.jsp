<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${requestScope.springLocale}" />
<fmt:setBundle basename="messages" />

<t:layout title="Upload Course Material">
    <script src="/js/upload-validator.js"></script>

    <div class="max-w-2xl mx-auto">
        <a href="/lecture/${lectureId}" class="inline-block mb-4 text-blue-600 hover:text-blue-800 font-medium">
            &larr; Back
        </a>

        <div class="bg-white rounded-lg shadow-md p-8">
            <h1 class="text-2xl font-bold text-gray-800 mb-8">Upload Course Material</h1>

            <form action="/lecture/${lectureId}/material/create" method="post" enctype="multipart/form-data">
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <input type="hidden" name="lectureId" value="${lectureId}" />
                <c:if test="${not empty error}">
                    <div class="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded-lg animate-pulse">
                        ${error}
                    </div>
                </c:if>

                <div class="mb-4">
                    <label for="fileName" class="block text-gray-700 font-medium mb-2">File Name</label>
                    <input type="text" 
                           id="fileName" name="fileName" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Enter file name (e.g. document.pdf)" />
                </div>

                <div class="mb-6">
                    <label for="file" class="block text-gray-700 font-medium mb-2">Select File</label>
                    <input type="file" 
                           id="file"
                           name="file"
                           required
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>

                <div class="flex gap-4">
                    <button type="submit"
                            class="bg-green-600 text-white font-medium py-2 px-4 rounded-lg hover:bg-green-700 transition-colors">
                        Upload
                    </button>
                    <a href="/lecture/${lectureId}"
                       class="bg-gray-300 text-gray-700 font-medium py-2 px-4 rounded-lg hover:bg-gray-400 transition-colors">
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>

</t:layout>
