<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${requestScope.springLocale}" />
<fmt:setBundle basename="messages" />

<t:layout title="index.title">

    <div class="flex flex-col h-full">
        <!-- Title Section -->
        <div class="mb-6">
            <h1 class="text-3xl font-extrabold text-gray-900 tracking-tight">
                <fmt:message key="index.welcome"/>
            </h1>
            <p class="text-lg text-gray-500 mt-1">
                <fmt:message key="index.description"/>
            </p>
        </div>

        <!-- Dynamic Content Area -->
        <div class="flex flex-1 flex-col lg:flex-row gap-6 max-h-[590px]">
            <!-- Side A: Lectures -->
            <div class="flex-1 bg-white border border-gray-200 rounded-xl shadow-md hover:shadow-lg transition-shadow overflow-hidden">
                <jsp:include page="lecturePollList.jsp">
                    <jsp:param name="type" value="lecture"/>
                </jsp:include>
            </div>

            <!-- Side B: Polls -->
            <div class="flex-1 bg-white border border-gray-200 rounded-xl shadow-md hover:shadow-lg transition-shadow overflow-hidden">
                <jsp:include page="lecturePollList.jsp">
                    <jsp:param name="type" value="poll"/>
                </jsp:include>
            </div>
        </div>
    </div>
</t:layout>
