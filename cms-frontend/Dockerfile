ARG BASE_IMAGE=base
ARG IMAGE=node:20.18.3-alpine

FROM ${BASE_IMAGE} AS builder

WORKDIR /app

COPY . .

RUN npm install -g pnpm@10.3.0 && pnpm run build

FROM builder AS lint

RUN pnpm run lint:ci

FROM builder AS test

RUN pnpm run test:ci

FROM ${IMAGE} AS runner

WORKDIR /app

ENV NODE_ENV=production

RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

COPY --from=builder /app/public ./public

COPY --from=builder --chown=nextjs:nodejs /app/.next/standalone ./
COPY --from=builder --chown=nextjs:nodejs /app/.next/static ./.next/static

USER nextjs

EXPOSE 3000

ENV HOSTNAME="0.0.0.0"
CMD ["node", "server.js"]