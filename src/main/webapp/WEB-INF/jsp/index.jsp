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

            <c:choose>
                <%-- STATE 1: USER IS LOGGED IN --%>
                <c:when test="${pageContext.request.userPrincipal != null}">
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
                </c:when>

                <%-- STATE 2: GUEST (NOT LOGGED IN) --%>
                <c:otherwise>
                    <!-- This container now spans the FULL WIDTH to allow centering -->
                    <div class="w-full flex items-center justify-center py-12">
                        <div class="max-w-md w-full bg-white border border-gray-200 rounded-2xl p-8 text-center shadow-lg">
                            <!-- Icon, Text, and Login Button here... -->
                            <div class="w-20 h-20 bg-blue-50 text-blue-600 rounded-full flex items-center justify-center mx-auto mb-6">
                                <svg class="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"></path>
                                </svg>
                            </div>
                            <h2 class="text-2xl font-bold text-gray-900 mb-2">
                                <fmt:message key="index.locked"/>
                            </h2>
                            <p class="text-gray-600 mb-8">
                                <fmt:message key="index.lockedGuide"/>
                            </p>
                            <a href="/login" class="block w-full py-3 px-4 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-lg transition-all shadow-md">
                                <fmt:message key="login.title"/>
                            </a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
    </div>
</t:layout>
