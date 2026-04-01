"use strict";
(() => {
  // src/main/webapp/ts/index.ts
  var validationRules = {
    username: [
      { test: (v) => /^[A-Za-z]/.test(v), message: "Username must start with a letter" },
      { test: (v) => /^[A-Za-z][0-9A-Za-z_-]{0,31}$/.test(v), message: "Username must contain only letters, digits, hyphens, and underscores (max 32 chars)" }
    ],
    password: [
      { test: (v) => v.length >= 6, message: "Password must be at least 6 characters" }
    ],
    email: [
      { test: (v) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v), message: "Invalid email format" }
    ],
    phone: [
      { test: (v) => /^\d{8}$/.test(v), message: "Phone number must be 8 digits" }
    ]
  };
  function validateField(name, value) {
    const rules = validationRules[name];
    if (!rules)
      return [];
    const errors = [];
    for (const rule of rules) {
      if (!rule.test(value)) {
        errors.push(rule.message);
      }
    }
    return errors;
  }
  function showFieldError(fieldId, errors) {
    const errorDiv = document.getElementById(`${fieldId}-error`);
    const field = document.getElementById(fieldId);
    if (!errorDiv || !field)
      return;
    if (errors.length > 0) {
      errorDiv.textContent = errors.join(", ");
      errorDiv.classList.remove("hidden");
      field.classList.add("border-red-500");
    } else {
      errorDiv.textContent = "";
      errorDiv.classList.add("hidden");
      field.classList.remove("border-red-500");
    }
  }
  function initFormValidation() {
    const form = document.querySelector("form[data-validate]");
    if (!form)
      return;
    const fields = form.querySelectorAll("input[data-validate]");
    fields.forEach((field) => {
      const fieldName = field.dataset.validate;
      const errorId = `${fieldName}-error`;
      if (!document.getElementById(errorId)) {
        const errorDiv = document.createElement("div");
        errorDiv.id = errorId;
        errorDiv.className = "text-red-500 text-sm mt-1 hidden";
        field.parentElement?.appendChild(errorDiv);
      }
      field.addEventListener("blur", () => {
        const errors = validateField(fieldName, field.value);
        showFieldError(fieldName, errors);
      });
      field.addEventListener("input", () => {
        showFieldError(fieldName, []);
      });
    });
    form.addEventListener("submit", (e) => {
      let hasErrors = false;
      fields.forEach((field) => {
        const fieldName = field.dataset.validate;
        const errors = validateField(fieldName, field.value);
        showFieldError(fieldName, errors);
        if (errors.length > 0)
          hasErrors = true;
      });
      if (hasErrors) {
        e.preventDefault();
      }
    });
  }
  function initPasswordToggle() {
    document.querySelectorAll("input[type='password'][data-toggle]").forEach((input) => {
      const toggleBtn = document.querySelector(`[data-toggle-for='${input.id}']`);
      if (!toggleBtn)
        return;
      toggleBtn.addEventListener("click", () => {
        if (input.type === "password") {
          input.type = "text";
          toggleBtn.textContent = "Hide";
        } else {
          input.type = "password";
          toggleBtn.textContent = "Show";
        }
      });
    });
  }
  document.addEventListener("DOMContentLoaded", () => {
    initFormValidation();
    initPasswordToggle();
  });
})();
//# sourceMappingURL=index.js.map
