FROM php:8.4-fpm-alpine

# Install Nginx, netcat (for nc in startup.sh), and other dependencies
RUN apk add --no-cache nginx wget netcat-openbsd

# Install required PHP extensions for Symfony
RUN apk --no-cache add libintl icu-dev \
    && docker-php-ext-configure intl \
    && docker-php-ext-install intl

RUN apk --no-cache add zip libzip-dev \
    && docker-php-ext-install zip

# Create Nginx runtime directory
RUN mkdir -p /run/nginx

# Copy Nginx configuration and startup script from docker/ folder
COPY docker/nginx.conf /etc/nginx/nginx.conf
COPY docker/startup.sh /app/docker/startup.sh

# Create app directory and copy project files
RUN mkdir -p /app
COPY . /app

# Install Composer
RUN sh -c "wget http://getcomposer.org/composer.phar && chmod a+x composer.phar && mv composer.phar /usr/local/bin/composer"

# Install Symfony dependencies (optimized for production)
RUN cd /app && \
    /usr/local/bin/composer install

# Set permissions for Symfony
RUN chown -R www-data:www-data /app \
    && chmod -R 755 /app/var /app/public

# Ensure startup script is executable
RUN chmod +x /app/docker/startup.sh

# Symfony-specific commands (uncomment and adjust as needed)
# RUN cd /app && \
#     php bin/console doctrine:migrations:migrate --no-interaction --force
# RUN cd /app && \
#     php bin/console cache:clear --env=prod --no-debug

# Set default PORT if not provided (e.g., 80)
ENV PORT=80

CMD ["/app/docker/startup.sh"]