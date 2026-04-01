/**
 * Online Course Website - TypeScript Entry Point
 *
 * This file contains minimal client-side TypeScript for:
 * - Form validation
 * - Basic UI effects
 *
 * Target: ES2015 (ES6) for broad browser compatibility
 */

// ========== Form Validation ==========

interface ValidationRule {
    test: (value: string) => boolean;
    message: string;
}

const validationRules: Record<string, ValidationRule[]> = {
    username: [
        { test: (v) => /^[A-Za-z]/.test(v), message: "Username must start with a letter" },
        { test: (v) => /^[A-Za-z][0-9A-Za-z_-]{0,31}$/.test(v), message: "Username must contain only letters, digits, hyphens, and underscores (max 32 chars)" },
    ],
    password: [
        { test: (v) => v.length >= 6, message: "Password must be at least 6 characters" },
    ],
    email: [
        { test: (v) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v), message: "Invalid email format" },
    ],
    phone: [
        { test: (v) => /^\d{8}$/.test(v), message: "Phone number must be 8 digits" },
    ],
};

/**
 * Validate a single field value
 */
function validateField(name: string, value: string): string[] {
    const rules = validationRules[name];
    if (!rules) return [];

    const errors: string[] = [];
    for (const rule of rules) {
        if (!rule.test(value)) {
            errors.push(rule.message);
        }
    }
    return errors;
}

/**
 * Show validation error for a field
 */
function showFieldError(fieldId: string, errors: string[]): void {
    const errorDiv = document.getElementById(`${fieldId}-error`);
    const field = document.getElementById(fieldId) as HTMLInputElement | null;

    if (!errorDiv || !field) return;

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

/**
 * Initialize form validation on page
 */
function initFormValidation(): void {
    const form = document.querySelector("form[data-validate]");
    if (!form) return;

    const fields = form.querySelectorAll<HTMLInputElement>("input[data-validate]");

    fields.forEach((field) => {
        const fieldName = field.dataset.validate!;
        const errorId = `${fieldName}-error`;

        // Create error div if not exists
        if (!document.getElementById(errorId)) {
            const errorDiv = document.createElement("div");
            errorDiv.id = errorId;
            errorDiv.className = "text-red-500 text-sm mt-1 hidden";
            field.parentElement?.appendChild(errorDiv);
        }

        // Validate on blur
        field.addEventListener("blur", () => {
            const errors = validateField(fieldName, field.value);
            showFieldError(fieldName, errors);
        });

        // Clear error on input
        field.addEventListener("input", () => {
            showFieldError(fieldName, []);
        });
    });

    // Prevent submit if invalid
    form.addEventListener("submit", (e) => {
        let hasErrors = false;

        fields.forEach((field) => {
            const fieldName = field.dataset.validate!;
            const errors = validateField(fieldName, field.value);
            showFieldError(fieldName, errors);
            if (errors.length > 0) hasErrors = true;
        });

        if (hasErrors) {
            e.preventDefault();
        }
    });
}

// ========== UI Effects ==========

/**
 * Toggle password visibility
 */
function initPasswordToggle(): void {
    document.querySelectorAll<HTMLInputElement>("input[type='password'][data-toggle]").forEach((input) => {
        const toggleBtn = document.querySelector(`[data-toggle-for='${input.id}']`);
        if (!toggleBtn) return;

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

/**
 * Initialize on DOM ready
 */
document.addEventListener("DOMContentLoaded", () => {
    initFormValidation();
    initPasswordToggle();
});

// Export for potential module use
export { validateField, showFieldError };
