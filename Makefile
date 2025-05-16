##@ Utility

.PHONY: help
help: ## Display this help
	@awk 'BEGIN {FS = ":.*##"; printf "\nUsage:\n  make \033[36m\033[0m\n"} /^[a-zA-Z0-9_-]+:.*?##/ { printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2 } /^##@/ { printf "\n\033[1m%s\033[0m\n", substr($$0, 5) } ' $(MAKEFILE_LIST)

##@ Dependencies

.PHONY: clean
clean: ## Removes the target/ directory (cleans previous builds)
	@mvn clean

.PHONY: compile
compile: ## Compiles the Java source code
	@mvn compile

.PHONY: unit
unit: ## Runs unit tests using JUnit/TestNG
	@mvn unit

.PHONY: test
test: ## Run entire test suite
	@mvn verify -Dgroups=int

.PHONY: package
package: ## Builds the project into a .jar or .war file
	@mvn package

.PHONY: install
install: ## Installs the package into the local Maven repository
	@mvn install

.PHONY: build
build: clean compile package ## Cleans, compiles, and packages the project
	@echo "Project built successfully."

.PHONY: database
database: ## Start Docker Desktop and run docker-compose
	@open -a Docker
	@echo "Waiting for Docker to start..."
	@while ! docker system info > /dev/null 2>&1; do sleep 1; done
	@echo "Docker is running!"
	@docker compose up -d

.PHONY: format
format: ## Format Project with Spotless
	@mvn spotless:apply

.PHONY: check
check: ## Check for formatting violations
	@mvn spotless:check
