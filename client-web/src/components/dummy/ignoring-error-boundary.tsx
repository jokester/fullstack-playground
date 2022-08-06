import React from 'react';

export class IgnoringErrorBoundary extends React.Component<{ children: React.ReactNode }, unknown> {
  componentDidCatch(error: unknown, errorInfo: React.ErrorInfo) {}

  render(): React.ReactNode {
    return this.props.children;
  }
}
