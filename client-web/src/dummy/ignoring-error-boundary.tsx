import React from 'react';

export class IgnoringErrorBoundary extends React.Component<Record<never, unknown>, unknown> {
  componentDidCatch(error: unknown, errorInfo: React.ErrorInfo) {}

  render(): React.ReactNode {
    return this.props.children;
  }
}
